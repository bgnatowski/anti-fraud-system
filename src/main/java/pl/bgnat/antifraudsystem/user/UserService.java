package pl.bgnat.antifraudsystem.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.exceptions.*;
import pl.bgnat.antifraudsystem.utils.validator.PhoneNumberValidator;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.enums.Role;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
	private final EmailService emailService;
	UserService(UserRepository userRepository,
				PasswordEncoder passwordEncoder,
				UserDTOMapper userDTOMapper, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDTOMapper = userDTOMapper;
		this.emailService = emailService;
	}

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
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String username = userRegistrationRequest.username();
		String email = userRegistrationRequest.email();

		if (isUserWithUsernameExists(username))
			throw new DuplicatedUsernameException(username);
		if (existsUserWithEmail(email))
			throw new DuplicatedUserEmailException(email);

		User createdUser = createProperUser(userRegistrationRequest);

		String confirmationCode = emailService.validateEmail(email);
		createdUser.setTemporaryAuthorization(
				TemporaryAuthorization.builder()
						.user(createdUser)
						.code(confirmationCode)
						.expirationDate(LocalDateTime.now().plusHours(24))
						.build()
		);

		User registeredUser = userRepository.save(createdUser);

		return userDTOMapper.apply(registeredUser);
	}

	UserDTO confirmUserEmail(String username, String code) {
		User user = findUserByUsername(username);

		LocalDateTime now = LocalDateTime.now();
		if(user.isAccountNonLocked())
			throw new UserIsAlreadyUnlockException(username, user.getEmail());

		TemporaryAuthorization temporaryAuthorization = user.getTemporaryAuthorization();
		LocalDateTime expirationDate = temporaryAuthorization.getExpirationDate();

		if(expirationDate.isBefore(now))
			throw new TemporaryAuthorizationException();
		if(!temporaryAuthorization.getCode().equals(code))
			throw new InvalidConfirmationCode(code);

		user.unlockAccount();
		userRepository.save(user);

		return userDTOMapper.apply(user);
	}

	UserDTO addUserPhone(String username, PhoneNumberRegisterRequest phone){
		User user = findUserByUsername(username);

		checkPhoneRequest(phone);

		String number = PhoneNumberValidator.extractDigits(phone.number());
		if(userRepository.existsUserByPhoneNumer(number))
			throw new PhoneNumberDuplicatedException(number);

		PhoneNumber userPhone = PhoneNumber.builder()
				.number(number)
				.user(user)
				.build();
		user.setPhone(userPhone);
		userRepository.save(user);
		return userDTOMapper.apply(user);
	}

	UserDTO addUserAddress(String username, AddressRegisterRequest address){
		User user = findUserByUsername(username);

		checkAddressRequest(address);

		try{
			Address userAddress = Address.builder()
					.addressLine1(address.addressLine1())
					.addressLine2(address.addressLine2())
					.city(address.city())
					.state(address.state())
					.country(Country.valueOf(address.country().toUpperCase()))
					.postalCode(address.postalCode())
					.user(user)
					.build();
			user.setAddress(userAddress);
		}catch (IllegalArgumentException e){
			throw new InvalidAddressFormatException(address.country()+" not supported");
		}
		userRepository.save(user);
		return userDTOMapper.apply(user);
	}

	UserDTO addCreditCardToUser(String username, CreditCard newCreditCard) {
		User user = findUserByUsername(username);

		validUserAccount(user);

		if(user.getAccount()==null)
			throw new CreditCardWithoutAccountException();

		newCreditCard.setOwner(user);
		newCreditCard.setAccount(user.getAccount());

//		if(user.getCreditCards().contains(newCreditCard))
//			throw new CreditCardAlreadyAssignedException(newCreditCard.getCardNumber());
//		user.getCreditCards().add(newCreditCard);

		userRepository.save(user);
		return userDTOMapper.apply(user);
	}

	UserDTO addAccountToUser(String username, Account newAccount) {
		User user = findUserByUsername(username);

		validUserAccount(user);
		if(user.getAccount()!=null && user.getAccount().equals(newAccount))
			throw new AccountAlreadyAssignedException(newAccount.toString());

		newAccount.setOwner(user);
		userRepository.save(user);

		return userDTOMapper.apply(user);
	}

	private static void validUserAccount(User user) {
		if(!user.isAccountNonLocked())
			throw new UserLockedException();
		if(user.getPhone() == null)
			throw new UserIncompletePhoneException();
		if(user.getAddress() == null)
			throw new UserIncompleteAddressException();
	}

	UserDeleteResponse deleteUserByUsername(String username) {
		if (!isUserWithUsernameExists(username))
			throw new UserNotFoundException(username);
		userRepository.deleteUserByUsername(username);
		return new UserDeleteResponse(username, DELETED_SUCCESSFULLY_RESPONSE);
	}

	UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
		try {
			String username = updateRequest.username();
			Role role = Role.valueOf(updateRequest.role());

			User user = findUserByUsername(username);

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

		User user = findUserByUsername(username);

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

	private boolean isUserWithUsernameExists(String username) {
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

	private User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
	}

	private boolean existsUserWithEmail(String email) {
		return userRepository.existsUserByEmail(email);
	}

	private boolean isTheSameRoleAlreadyAssigned(Role role, User user) {
		return user.getRole().equals(role);
	}
	private boolean isSupportOrMerchant(Role role) {
		return Role.SUPPORT.equals(role) || Role.MERCHANT.equals(role);
	}

	private boolean isValidRequestJsonFormat(UserRegistrationRequest userRegistrationRequest) {
		return Stream.of(userRegistrationRequest.firstName(),
						userRegistrationRequest.lastName(),
						userRegistrationRequest.email(),
						userRegistrationRequest.username(),
						userRegistrationRequest.password())
				.noneMatch(Objects::isNull);
	}

	private boolean isValidChangeLockRequest(String username, String operation) {
		return operation == null || username == null;
	}

	private boolean isAdministrator(User user) {
		return Role.ADMINISTRATOR.equals(user.getRole());
	}

	private void checkPhoneRequest(PhoneNumberRegisterRequest phone) {
		if(!isValidPhoneNumberRequest(phone))
			throw new InvalidPhoneFormatException();
		if (!PhoneNumberValidator.isValid(phone.number()))
			throw new InvalidPhoneFormatException(phone.number());
	}

	private static boolean isValidPhoneNumberRequest(PhoneNumberRegisterRequest phone) {
		return phone != null;
	}
	private void checkAddressRequest(AddressRegisterRequest address) {
		if(!isValidAddressRequest(address))
			throw new InvalidAddressFormatException();
		if (!isValidAddress(address))
			throw new InvalidAddressFormatException(address.toString());
	}

	private static boolean isValidAddressRequest(AddressRegisterRequest address) {
		return address != null;
	}

	private boolean isValidAddress(AddressRegisterRequest address) {
		return Stream.of(
								address.addressLine1(),
								address.country(),
								address.city(),
								address.postalCode(),
								address.state())
						.noneMatch(s-> s==null || s.isEmpty());
	}
}
