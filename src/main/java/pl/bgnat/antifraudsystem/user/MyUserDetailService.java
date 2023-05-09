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
import pl.bgnat.antifraudsystem.user.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.response.UserUnlockResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {
	public static final long ADMINISTRATOR_ID = 1L;
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final UserDTOMapper userDTOMapper;

	public MyUserDetailService(@Qualifier("jdbc") UserDao userDao,
							   PasswordEncoder passwordEncoder,
							   UserDTOMapper userDTOMapper) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.selectUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
	}

	public UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		validRegistrationRequest(userRegistrationRequest);

		String username = userRegistrationRequest.username();
		validIfUserAlreadyExist(username);

		User user;
		if(!doesTheAdministratorExist())
			user = createAdministrator(userRegistrationRequest);
		else
			user = createUser(userRegistrationRequest);

		//register
		userDao.insertUser(user);
		return userDao.selectUserByUsername(username)
				.map(userDTOMapper)
				.orElseThrow(() -> new ResourceNotFoundException("User with username = " + username + " not found"));
	}

	public List<UserDTO> getAllRegisteredUsers() {
		return userDao.selectAllUsers()
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}

	public UserDeleteResponse deleteUserByUsername(String username) {
		doesTheUserExist(username);
		userDao.deleteUserByUsername(username);
		return new UserDeleteResponse(username, "Deleted successfully!");

	}

	public UserDTO changeRole(UserRoleUpdateRequest updateRequest) {
		try {
			String username = updateRequest.username();
			Role role = Role.valueOf(updateRequest.role());

			User user = userDao.selectUserByUsername(username)
					.orElseThrow(() -> new ResourceNotFoundException("There is no user with username = " + username));
			if (!isSupportOrMerchant(role))
				throw new RequestValidationException("The role should be SUPPORT or MERCHANT");
			if (isTheSameRoleAlreadyAssigned(role, user))
				throw new DuplicateResourceException("Role: " + role + " is already assigned to the user: " + username);

			user.setRole(role);
			userDao.updateUser(user);

			return userDTOMapper.apply(user);
		} catch (IllegalArgumentException e) {
			throw new RequestValidationException("Invalid role: " + updateRequest.role());
		}
	}

	public UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		String username = updateRequest.username();
		String operation = updateRequest.operation();

		User user = userDao.selectUserByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("There is no user with username = " + username));

		if(isAdministrator(user))
			throw new RequestValidationException("Cannot block administrator!");

		switch (operation){
			case "LOCK" -> user.setAccountNonLocked(false);
			case "UNLOCK" -> user.setAccountNonLocked(true);
			default -> throw new RequestValidationException("Invalid operation: " + operation);
		}

		//update
		userDao.updateUser(user);

		String operationResult = "LOCK".equals(operation) ? "locked!" : "unlocked!";
		return new UserUnlockResponse(String.format("User %s %s", username, operationResult));
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
			throw new ResourceNotFoundException("There is no user with username = " + username);
	}

	private void validIfUserAlreadyExist(String username) {
		if (userDao.existsUserWithUsername(username)) {
			throw new DuplicateResourceException("User with username = " + username + " already exists");
		}
	}

	private boolean doesTheAdministratorExist() {
		return userDao.existsUserById(ADMINISTRATOR_ID);
	}

	private static void validRegistrationRequest(UserRegistrationRequest userRegistrationRequest) {
		if (userRegistrationRequest.name() == null ||
				userRegistrationRequest.username() == null ||
				userRegistrationRequest.password() == null)
			throw new RequestValidationException("Wrong json format");
	}
}
