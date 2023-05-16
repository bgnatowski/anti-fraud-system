package pl.bgnat.antifraudsystem.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.user.DuplicatedUserException;
import pl.bgnat.antifraudsystem.exception.user.UserNotFoundException;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.request.UserRoleUpdateRequest;
import pl.bgnat.antifraudsystem.user.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.response.UserUnlockResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
import static pl.bgnat.antifraudsystem.user.UserCreator.createAdministrator;
import static pl.bgnat.antifraudsystem.user.UserCreator.createMerchant;

@Service
public class UserService {
	public static final long ADMINISTRATOR_ID = 1L;
	public static final String CANNOT_BLOCK_ADMINISTRATOR = "Cannot block administrator!";
	public static final String INVALID_OPERATION_S = "Invalid operation: %s";
	public static final String INVALID_ROLE_S = "Invalid role: %s";
	public static final String INVALID_REQUEST = "Invalid request form";
	public static final String THE_ROLE_SHOULD_BE_SUPPORT_OR_MERCHANT = "The role should be SUPPORT or MERCHANT";
	public static final String ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S = "Role: %s is already assigned to the user with username = %s";
	public static final String DELETED_SUCCESSFULLY_RESPONSE = "Deleted successfully!";
	public static final String USER_UNLOCK_RESPONSE = "User %s %s";
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
	private final UserDTOMapper userDTOMapper;

	public UserService(@Qualifier("jpa") UserDao userDao,
					   PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
	}

	public List<UserDTO> getAllRegisteredUsers() {
		return userDao.selectAllUsers()
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}

	public UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		
		String username = userRegistrationRequest.username();

		if (isUserWithUsernameAlreadyRegistered(username)) {
			throw new DuplicatedUserException(username);
		}

		User user = createProperUser(userRegistrationRequest);

		//register user
		User registeredUser = userDao.insertUser(user);
		return userDTOMapper.apply(registeredUser);
	}

	public UserDeleteResponse deleteUserByUsername(String username) {
		if (!isUserWithUsernameAlreadyRegistered(username))
			throw new UserNotFoundException(username);
		userDao.deleteUserByUsername(username);
		return new UserDeleteResponse(username, DELETED_SUCCESSFULLY_RESPONSE);
	}

	public UserDTO changeRole(UserRoleUpdateRequest updateRequest) {
		try {
			String username = updateRequest.username();
			Role role = Role.valueOf(updateRequest.role());

			User user = getUserByUserName(username);

			if (!isSupportOrMerchant(role))
				throw new RequestValidationException(
						THE_ROLE_SHOULD_BE_SUPPORT_OR_MERCHANT);
			if (isTheSameRoleAlreadyAssigned(role, user))
				throw new DuplicateResourceException(
						String.format(ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S, role, username));

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

		if (isValidChangeLockRequest(username, operation))
			throw new RequestValidationException(String.format(INVALID_REQUEST));

		User user = getUserByUserName(username);

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
		return new UserUnlockResponse(String.format(USER_UNLOCK_RESPONSE, username, operationResult));
	}

	private boolean isValidRequestJsonFormat(UserRegistrationRequest userRegistrationRequest) {
		return Stream.of(userRegistrationRequest.name(),
						userRegistrationRequest.username(),
						userRegistrationRequest.password())
				.noneMatch(Objects::isNull);
	}
	private boolean isUserWithUsernameAlreadyRegistered(String username) {
		return userDao.existsUserWithUsername(username);
	}
	private User createProperUser(UserRegistrationRequest userRegistrationRequest) {
		if (!doesTheAdministratorExist())
			return createAdministrator(userRegistrationRequest, passwordEncoder);
		return createMerchant(userRegistrationRequest, passwordEncoder);
	}
	private boolean doesTheAdministratorExist() {
		return userDao.existsUserById(ADMINISTRATOR_ID);
	}
	private User getUserByUserName(String username) {
		return userDao.selectUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}
	private boolean isTheSameRoleAlreadyAssigned(Role role, User user) {
		return user.getRole().equals(role);
	}
	private boolean isSupportOrMerchant(Role role) {
		return Role.SUPPORT.equals(role) || Role.MERCHANT.equals(role);
	}
	private boolean isValidChangeLockRequest(String username, String operation) {
		return operation == null || username == null;
	}
	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}
}
