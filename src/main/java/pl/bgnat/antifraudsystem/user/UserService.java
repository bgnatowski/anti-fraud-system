package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.user.enums.Role;
import pl.bgnat.antifraudsystem.user.exceptions.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest.LOCK;
import static pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest.UNLOCK;
import static pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse.DELETED_SUCCESSFULLY_RESPONSE;
import static pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse.MESSAGE_PATTERN;

@Service
@RequiredArgsConstructor
class UserService {
	private static final long ADMINISTRATOR_ID = 1L;
	private final UserRepository userRepository;
	private final UserDTOMapper userDTOMapper;
	private final UserCreator userCreator;
	private final PhoneNumberCreator phoneNumberCreator;
	private final AddressCreator addressCreator;
	private final TemporaryAuthorizationCreator temporaryAuthorizationCreator;
	private final UserValidator userValidator;

	List<UserDTO> getAllRegisteredUsers() {
		Page<User> page = userRepository.findAll(Pageable.ofSize(100));
		return page.getContent()
				.stream()
				.map(userDTOMapper)
				.sorted(Comparator.comparingLong(UserDTO::id))
				.collect(Collectors.toList());
	}

	UserDTO getUserByUsername(String username) {
		return userDTOMapper.apply(findUserByUsername(username));
	}

	UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		userValidator.validRegistration(userRegistrationRequest);

		User createdUser = createProperUser(userRegistrationRequest);

		String phoneNumber = PhoneNumber.extractDigits(userRegistrationRequest.phoneNumber());
		PhoneNumber userPhone = phoneNumberCreator.createPhoneNumber(createdUser, phoneNumber);
		createdUser.setPhone(userPhone);

		TemporaryAuthorization temporaryAuthorization = temporaryAuthorizationCreator.createTemporaryAuthorization(createdUser);
		createdUser.setTemporaryAuthorization(temporaryAuthorization);

		User registeredUser = userRepository.save(createdUser);

		return userDTOMapper.apply(registeredUser);
	}


	UserDTO addUserAddress(String username, AddressRegisterRequest address) {

		User user = findUserByUsername(username);
		Address userAddress = addressCreator.createAddress(user, address);

		user.setAddress(userAddress);
		userRepository.save(user);

		return userDTOMapper.apply(user);

	}

	UserDTO addCreditCardToUser(String username, CreditCard newCreditCard) {
		User user = findUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountExists(user.getAccount());


		newCreditCard.setOwner(user);
		newCreditCard.setAccount(user.getAccount());

		userRepository.save(user);
		return userDTOMapper.apply(user);
	}

	UserDTO addAccountToUser(String username, Account newAccount) {
		User user = findUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountNonExist(user, newAccount);

		newAccount.setOwner(user);

		userRepository.save(user);

		return userDTOMapper.apply(user);
	}


	UserDeleteResponse deleteUserByUsername(String username) {
		userValidator.validUserExistsByUsername(username);

		userRepository.deleteUserByUsername(username);
		return UserDeleteResponse.builder()
				.username(username)
				.status(DELETED_SUCCESSFULLY_RESPONSE)
				.build();
	}

	UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
		String username = updateRequest.username();
		String roleString = updateRequest.role();

		User user = findUserByUsername(username);
		Role role = Role.parse(roleString);

		userValidator.validChangeRole(role, user);

		user.setRole(role);
		userRepository.save(user);

		return userDTOMapper.apply(user);

	}


	UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		String username = updateRequest.username();
		String operation = updateRequest.operation();

		User user = findUserByUsername(username);

		if (isAdministrator(user))
			throw new AdministratorCannotBeLockException();

		switch (operation) {
			case LOCK -> user.lockAccount();
			case UNLOCK -> user.unlockAccount();
			default -> throw new IllegalChangeLockOperationException(operation);
		}

		//update
		userRepository.save(user);

		String operationResult = LOCK.equals(operation) ? "locked!" : "unlocked!";
		String statusMessage = String.format(MESSAGE_PATTERN, username, operationResult);
		return UserUnlockResponse.builder()
				.status(statusMessage)
				.build();
	}

	private User createProperUser(UserRegistrationRequest userRegistrationRequest) {
		if (!userRepository.existsById(ADMINISTRATOR_ID))
			return userCreator.createAdministrator(userRegistrationRequest);
		return userCreator.createMerchant(userRegistrationRequest);
	}

	private User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}

	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}
}
