package pl.bgnat.antifraudsystem.bank.user.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;
import pl.bgnat.antifraudsystem.bank.user.exceptions.AdministratorCannotBeLockException;
import pl.bgnat.antifraudsystem.bank.user.exceptions.UserNotFoundException;
import pl.bgnat.antifraudsystem.bank.user.exceptions.IllegalChangeLockOperationException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class UserService {
	private final UserRepository userRepository;
	private final UserDTOMapper userDTOMapper;
	private final UserValidator userValidator;
	private final PasswordEncoder passwordEncoder;

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
		createdUser.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));

		PhoneNumber userPhone = PhoneNumberCreator.createPhoneNumber(createdUser, userRegistrationRequest.phoneNumber());
		createdUser.setPhone(userPhone);

		if(createdUser.getRole().equals(Role.MERCHANT) && !createdUser.getUsername().equals("JohnDoe2")){ //todo delete and
			TemporaryAuthorization temporaryAuthorization =
					TemporaryAuthorizationCreator.createTemporaryAuthorization(createdUser);
			createdUser.setTemporaryAuthorization(temporaryAuthorization);
		}

		User registeredUser = userRepository.save(createdUser);

		return registeredUser;
	}

	User addUserAddress(String username, AddressRegisterRequest address) {
		User user = getUserByUsername(username);
		Address userAddress = AddressCreator.createAddress(user, address);

		user.setAddress(userAddress);
		userRepository.save(user);

		return user;

	}

	User addAccountToUser(String username, Account newAccount) {
		User user = getUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountNonExist(user);

		newAccount.setOwner(user);
		user.setHasAccount(true);

		userRepository.save(user);

		return user;
	}

	User addCreditCardToUser(String username, CreditCard newCreditCard) {
		User user = getUserByUsername(username);

		userValidator.validUserProfile(user);
		userValidator.validAccountExists(user.getAccount());

		newCreditCard.setAccount(user.getAccount());
		newCreditCard.setCountry(user.getAccount().getCountry());

		if(!user.isHasAnyCreditCard())
			user.setHasAnyCreditCard(true);
		user.increaseCreditCardNumber();

		userRepository.save(user);
		return user;
	}


	UserDeleteResponse deleteUserByUsername(String username) {
		userValidator.validUserExistsByUsername(username);

		userRepository.deleteUserByUsername(username);

		return UserDeleteResponse.builder()
				.username(username)
				.status(UserDeleteResponse.DELETED_SUCCESSFULLY_RESPONSE)
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
			case UserUnlockRequest.LOCK -> user.lockAccount();
			case UserUnlockRequest.UNLOCK -> user.unlockAccount();
			default -> throw new IllegalChangeLockOperationException(operation);
		}

		//update
		userRepository.save(user);

		String operationResult = UserUnlockRequest.LOCK.equals(operation) ? "locked!" : "unlocked!";
		String statusMessage = String.format(UserUnlockResponse.MESSAGE_PATTERN, username, operationResult);
		return UserUnlockResponse.builder()
				.status(statusMessage)
				.build();
	}
	UserDTO mapToDto(User user){
		return userDTOMapper.apply(user);
	}

	private User createProperUser(UserRegistrationRequest userRegistrationRequest) {
		if (!userRepository.existsUserByRole(Role.ADMINISTRATOR))
			return UserCreator.createAdministrator(userRegistrationRequest);
		return UserCreator.createMerchant(userRegistrationRequest);
	}

	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}
}
