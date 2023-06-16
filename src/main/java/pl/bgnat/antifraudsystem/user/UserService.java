package pl.bgnat.antifraudsystem.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.exceptions.DuplicatedUserException;
import pl.bgnat.antifraudsystem.user.exceptions.UserNotFoundException;
import pl.bgnat.antifraudsystem.utils.PhoneNumberValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.INVALID_REQUEST;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
import static pl.bgnat.antifraudsystem.user.UserCreator.createAdministrator;
import static pl.bgnat.antifraudsystem.user.UserCreator.createMerchant;

@Service
class UserService {
	private static final long ADMINISTRATOR_ID = 1L;
	static final String CANNOT_BLOCK_ADMINISTRATOR = "Cannot block administrator!";
	static final String INVALID_OPERATION_S = "Invalid operation: %s";
	static final String INVALID_ROLE_S = "Invalid role: %s";
	static final String THE_ROLE_SHOULD_BE_SUPPORT_OR_MERCHANT = "The role should be SUPPORT or MERCHANT";
	static final String ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S = "Role: %s is already assigned to the user with username = %s";
	static final String DELETED_SUCCESSFULLY_RESPONSE = "Deleted successfully!";
	static final String USER_UNLOCK_RESPONSE = "User %s %s";
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDTOMapper userDTOMapper;

	UserService(UserRepository userRepository,
				PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
	}

	List<UserDTO> getAllRegisteredUsers() {
		Page<User> page = userRepository.findAll(Pageable.ofSize(100));
		return page.getContent()
				.stream()
				.map(userDTOMapper)
				.sorted(Comparator.comparingLong(UserDTO::id))
				.collect(Collectors.toList());
	}

	UserDTO registerUser(UserRegistrationRequest userRegistrationRequest,
						 PhoneNumberRegisterRequest phone,
						 AddressRegisterRequest address) {
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		User createdUser = createUser(userRegistrationRequest);
		if (isValidPhone(phone)) {
			PhoneNumber userPhone = PhoneNumber.builder()
					.number(PhoneNumberValidator.extractDigits(phone.number()))
					.user(createdUser)
					.build();
			createdUser.setPhone(userPhone);
		}
		if (isValidAddress(address)) {
			Address userAddress = Address.builder()
					.addressLine1(address.addressLine1())
					.addressLine2(address.addressLine2())
					.city(address.city())
					.state(address.state())
					.country(Country.valueOf(address.country()))
					.postalCode(address.postalCode())
					.user(createdUser)
					.build();
			createdUser.setAddress(userAddress);
		}

		User registeredUser = userRepository.save(createdUser);
		return userDTOMapper.apply(registeredUser);
	}

	private boolean isValidAddress(AddressRegisterRequest address) {
		return address != null
				&&
				Stream.of(
						address.addressLine1(),
						address.country(),
						address.city(),
						address.postalCode(),
						address.state())
				.noneMatch(Objects::isNull);
	}

	private boolean isValidPhone(PhoneNumberRegisterRequest phone) {
		return (phone.number() != null) && PhoneNumberValidator.isPhoneNumberValid(phone.number());
	}


	User createUser(UserRegistrationRequest userRegistrationRequest) {
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String username = userRegistrationRequest.username();

		if (isUserWithUsernameAlreadyRegistered(username)) {
			throw new DuplicatedUserException(username);
		}

		return createProperUser(userRegistrationRequest);
	}

	UserDeleteResponse deleteUserByUsername(String username) {
		if (!isUserWithUsernameAlreadyRegistered(username))
			throw new UserNotFoundException(username);
		userRepository.deleteUserByUsername(username);
		return new UserDeleteResponse(username, DELETED_SUCCESSFULLY_RESPONSE);
	}

	UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
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
			userRepository.save(user);

			return userDTOMapper.apply(user);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new RequestValidationException(String.format(INVALID_ROLE_S, updateRequest.role()));
		}
	}

	UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
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
		userRepository.save(user);

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
		return userRepository.existsUserByUsername(username);
	}

	private User createProperUser(UserRegistrationRequest userRegistrationRequest) {
		if (!doesTheAdministratorExist())
			return createAdministrator(userRegistrationRequest, passwordEncoder);
		return createMerchant(userRegistrationRequest, passwordEncoder);
	}

	private boolean doesTheAdministratorExist() {
		return userRepository.existsById(ADMINISTRATOR_ID);
	}

	private User getUserByUserName(String username) {
		return userRepository.findUserByUsername(username)
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
