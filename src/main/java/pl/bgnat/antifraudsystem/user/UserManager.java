package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.user.exceptions.InvalidAddressFormatException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
import static pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest.UNLOCK;
import static pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse.EMAIL_CONFIRMED_MESSAGE;

@Service
@RequiredArgsConstructor
class UserManager {
	private final UserService userService;
	private final EmailService emailService;
	private final CreditCardService creditCardService;
	private final TemporaryAuthorizationService temporaryAuthorizationService;
	private final AccountService accountService;

	UserEmailConfirmedResponse confirmUserEmail(String username, String code) {
		UserDTO user = userService.getUserByUsername(username);

		if(user.isActive())
			throw new RequestValidationException("user is already active");

		TemporaryAuthorizationDTO userTemporaryAuthorization =
				temporaryAuthorizationService.getTemporaryAuthorization(username);

		emailService.confirmEmail(user, userTemporaryAuthorization, code);
		changeLock(new UserUnlockRequest(username, UNLOCK));
		return UserEmailConfirmedResponse
				.builder()
				.message(EMAIL_CONFIRMED_MESSAGE)
				.build();
	}

	UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		UserDTO registeredUser = userService.registerUser(userRegistrationRequest);
		TemporaryAuthorizationDTO temporaryAuthorizationDTO =
				temporaryAuthorizationService.getTemporaryAuthorization(registeredUser.username());

		emailService.sendConfirmationEmail(registeredUser.email(), temporaryAuthorizationDTO.code());

		return registeredUser;
	}

	UserDTO addUserAddress(String username, AddressRegisterRequest addressRegisterRequest) {
		if (!isValidAddressRequest(addressRegisterRequest))
			throw new InvalidAddressFormatException(addressRegisterRequest.toString());

		return userService.addUserAddress(username, addressRegisterRequest);
	}


	UserDTO addCreditCardToUser(String username) {
		CreditCard newCreditCard = creditCardService.createCreditCard();
		UserDTO userDTO = userService.addCreditCardToUser(username, newCreditCard);
		emailService.sendCreditCardPin(userDTO.email(), newCreditCard.getPin());
		return userDTO;
	}

	UserDTO getUserByUsername(String username) {
		return userService.getUserByUsername(username);
	}

	UserDTO addAccountToUser(String username) {
		UserDTO user = getUserByUsername(username);
		Account newAccount = accountService.createAccount(user);
		return userService.addAccountToUser(username, newAccount);
	}

	List<UserDTO> getAllRegisteredUsers() {
		return userService.getAllRegisteredUsers();
	}

	UserDeleteResponse deleteUserByUsername(String username) {
		return userService.deleteUserByUsername(username);
	}

	UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
		return userService.changeRole(updateRequest);
	}

	UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		if (isValidChangeLockRequest(updateRequest))
			throw new RequestValidationException(String.format(WRONG_JSON_FORMAT));

		return userService.changeLock(updateRequest);
	}


	private boolean isValidRequestJsonFormat(UserRegistrationRequest userRegistrationRequest) {
		return Stream.of(userRegistrationRequest.firstName(),
						userRegistrationRequest.lastName(),
						userRegistrationRequest.email(),
						userRegistrationRequest.username(),
						userRegistrationRequest.password(),
						userRegistrationRequest.phoneNumber(),
						userRegistrationRequest.dateOfBirth())
				.noneMatch(Objects::isNull);
	}

	private static boolean isValidAddressRequest(AddressRegisterRequest addressRegisterRequest) {
		return Stream.of(
						addressRegisterRequest.addressLine1(),
						addressRegisterRequest.country(),
						addressRegisterRequest.city(),
						addressRegisterRequest.postalCode(),
						addressRegisterRequest.state())
				.noneMatch(s -> s == null || s.isEmpty());
	}

	private boolean isValidChangeLockRequest(UserUnlockRequest updateRequest) {
		return Stream.of(updateRequest.operation(),
						updateRequest.username())
				.noneMatch(s -> s == null || s.isEmpty());
	}

}
