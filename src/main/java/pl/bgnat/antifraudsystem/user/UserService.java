package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.user.enums.Role;
import pl.bgnat.antifraudsystem.user.exceptions.AdministratorCannotBeLockException;
import pl.bgnat.antifraudsystem.user.exceptions.IllegalChangeLockOperationException;
import pl.bgnat.antifraudsystem.user.exceptions.UserNotFoundException;

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
	private final UserCreator userCreator;
	private final UserDTOMapper userDTOMapper;
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

	User getUserByUsername(String username) {
		return userRepository.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}

	User registerUser(UserRegistrationRequest userRegistrationRequest) {
		userValidator.validRegistration(userRegistrationRequest);

		User createdUser = createProperUser(userRegistrationRequest);

		String[] phoneNumber = PhoneNumber.extractAreaCodeAndNumber(userRegistrationRequest.phoneNumber());
		PhoneNumber userPhone = phoneNumberCreator.createPhoneNumber(createdUser, phoneNumber);
		createdUser.setPhone(userPhone);

		if(createdUser.getRole().equals(Role.MERCHANT) && !createdUser.getUsername().equals("JohnDoe2")){
			TemporaryAuthorization temporaryAuthorization =
					temporaryAuthorizationCreator.createTemporaryAuthorization(createdUser);
			createdUser.setTemporaryAuthorization(temporaryAuthorization);
		}

		User registeredUser = userRepository.save(createdUser);

		return registeredUser;
	}


	User addUserAddress(String username, AddressRegisterRequest address) {

		User user = getUserByUsername(username);
		Address userAddress = addressCreator.createAddress(user, address);

		user.setAddress(userAddress);
		userRepository.save(user);

		return user;

	}

	User addAccountToUser(String username, Account newAccount) {
		User user = getUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountNonExist(user, newAccount);

		newAccount.setOwner(user);
		userRepository.save(user);

		return user;
	}

	User addCreditCardToUser(String username, CreditCard newCreditCard) {
		User user = getUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountExists(user.getAccount());

		newCreditCard.setOwner(user);
		newCreditCard.setAccount(user.getAccount());
		newCreditCard.setCountry(user.getAccount().getCountry());

		userRepository.save(user);
		return user;
	}


	UserDeleteResponse deleteUserByUsername(String username) {
		userValidator.validUserExistsByUsername(username);

		userRepository.deleteUserByUsername(username);
		return UserDeleteResponse.builder()
				.username(username)
				.status(DELETED_SUCCESSFULLY_RESPONSE)
				.build();
	}

	User changeRole(String username, String roleString) {

		User user = getUserByUsername(username);
		Role role = Role.parse(roleString);

		userValidator.validChangeRole(role, user);

		user.setRole(role);
		userRepository.save(user);

		return user;

	}

	UserUnlockResponse changeLock(String username, String operation) {
		User user = getUserByUsername(username);

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

	public UserDTO mapToDto(User user){
		return userDTOMapper.apply(user);
	}

	private User createProperUser(UserRegistrationRequest userRegistrationRequest) {
		if (!userRepository.existsById(ADMINISTRATOR_ID))
			return userCreator.createAdministrator(userRegistrationRequest);
		return userCreator.createMerchant(userRegistrationRequest);
	}
	

	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}
}
