package pl.bgnat.antifraudsystem.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.request.UserRoleUpdateRequest;
import pl.bgnat.antifraudsystem.user.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.response.UserUnlockResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {
	public static final long ADMINISTRATOR_ID = 1L;
	public static final String USER_WITH_USERNAME_S_ALREADY_EXISTS = "User with username = %s already exists";
	public static final String THERE_IS_NO_USER_WITH_USERNAME_S = "There is no user with username = %s";
	public static final String WRONG_JSON_FORMAT = "Wrong json format";
	public static final String USER_UNLOCK_RESPONSE_TEXT = "User %s %s";
	public static final String INVALID_OPERATION_S = "Invalid operation: %s";
	public static final String CANNOT_BLOCK_ADMINISTRATOR = "Cannot block administrator!";
	public static final String INVALID_ROLE_S = "Invalid role: %s";
	public static final String THE_ROLE_SHOULD_BE_SUPPORT_OR_MERCHANT = "The role should be SUPPORT or MERCHANT";
	public static final String ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S = "Role: %s is already assigned to the user with username = %s";
	public static final String DELETED_SUCCESSFULLY = "Deleted successfully!";
	private static final String INVALID_REQUEST = "Invalid request form";
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final UserDTOMapper userDTOMapper;

	public MyUserDetailService(@Qualifier("jpa") UserDao userDao,
							   PasswordEncoder passwordEncoder,
							   UserDTOMapper userDTOMapper) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.selectUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username)));
	}

	public List<UserDTO> getAllRegisteredUsers() {
		return userDao.selectAllUsers()
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}

	public UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		validRegistrationRequest(userRegistrationRequest);

		String username = userRegistrationRequest.username();
		validIfUserAlreadyExist(username);

		User user;
		if (!doesTheAdministratorExist())
			user = createAdministrator(userRegistrationRequest);
		else
			user = createUser(userRegistrationRequest);

		//register
		return userDTOMapper.apply(userDao.insertUser(user));
	}

	public UserDeleteResponse deleteUserByUsername(String username) {
		doesTheUserExist(username);
		userDao.deleteUserByUsername(username);
		return new UserDeleteResponse(username, DELETED_SUCCESSFULLY);
	}

	public UserDTO changeRole(UserRoleUpdateRequest updateRequest) {
		try {
			String username = updateRequest.username();
			Role role = Role.valueOf(updateRequest.role());

			User user = userDao.selectUserByUsername(username)
					.orElseThrow(() -> new ResourceNotFoundException(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username)));
			if (!isSupportOrMerchant(role))
				throw new RequestValidationException(THE_ROLE_SHOULD_BE_SUPPORT_OR_MERCHANT);
			if (isTheSameRoleAlreadyAssigned(role, user))
				throw new DuplicateResourceException(String.format(ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S, role, username));

			user.setRole(role);
			userDao.updateUser(user);

			return userDTOMapper.apply(user);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new RequestValidationException(String.format(INVALID_ROLE_S, updateRequest.role()));
		}
	}

	public UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		String username = updateRequest.username();
		String operation = updateRequest.operation();

		if(operation==null || username==null)
			throw new RequestValidationException(String.format(INVALID_REQUEST));

		User user = userDao.selectUserByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username)));

		if (isAdministrator(user))
			throw new RequestValidationException(CANNOT_BLOCK_ADMINISTRATOR);

		switch (operation) {
			case "LOCK" -> user.lockAccount();
			case "UNLOCK" -> user.unlockAccount();
			default -> throw new RequestValidationException(String.format(INVALID_OPERATION_S, operation));
		}

		//update
		userDao.updateUser(user);

		String operationResult = "LOCK".equals(operation) ? "locked!" : "unlocked!";
		return new UserUnlockResponse(String.format(USER_UNLOCK_RESPONSE_TEXT, username, operationResult));
	}

	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}

	private static boolean isTheSameRoleAlreadyAssigned(Role role, User user) {
		return user.getRole().equals(role);
	}

	private static boolean isSupportOrMerchant(Role role) {
		return Role.SUPPORT.equals(role) || Role.MERCHANT.equals(role);
	}

	private User createAdministrator(UserRegistrationRequest userRegistrationRequest) {
		return new User(userRegistrationRequest.name(),
				userRegistrationRequest.username(),
				passwordEncoder.encode(userRegistrationRequest.password()),
				Role.ADMINISTRATOR,
				true);
	}

	private User createUser(UserRegistrationRequest userRegistrationRequest) {
		User user;
		user = new User(userRegistrationRequest.name(),
				userRegistrationRequest.username(),
				passwordEncoder.encode(userRegistrationRequest.password()),
				Role.MERCHANT,
				false);
		return user;
	}

	private void doesTheUserExist(String username) {
		if (!userDao.existsUserWithUsername(username))
			throw new ResourceNotFoundException(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));
	}

	private void validIfUserAlreadyExist(String username) {
		if (userDao.existsUserWithUsername(username)) {
			throw new DuplicateResourceException(String.format(USER_WITH_USERNAME_S_ALREADY_EXISTS, username));
		}
	}

	private boolean doesTheAdministratorExist() {
		return userDao.existsUserById(ADMINISTRATOR_ID);
	}

	private static void validRegistrationRequest(UserRegistrationRequest userRegistrationRequest) {
		if (userRegistrationRequest.name() == null ||
				userRegistrationRequest.username() == null ||
				userRegistrationRequest.password() == null)
			throw new RequestValidationException(WRONG_JSON_FORMAT);
	}
}
