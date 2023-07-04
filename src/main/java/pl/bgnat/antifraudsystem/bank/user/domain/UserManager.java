package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.*;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;
import pl.bgnat.antifraudsystem.bank.user.exceptions.InvalidAddressFormatException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
@RequiredArgsConstructor
class UserManager {
	private final UserService userService;
	private final EmailService emailService;
	private final CreditCardService creditCardService;
	private final AccountService accountService;
	private final TemporaryAuthorizationService temporaryAuthorizationService;
	UserEmailConfirmedResponse confirmUserEmail(ConfirmEmailRequest confirmEmailRequest) {
		if (!isValidConfirmEmailRequest(confirmEmailRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String username = confirmEmailRequest.username();
		String code = confirmEmailRequest.code();

		TemporaryAuthorization userTemporaryAuthorization =
				temporaryAuthorizationService.getTemporaryAuthorization(username);

		emailService.confirmEmail(userTemporaryAuthorization, code);
		userService.changeLock(username, UserUnlockRequest.UNLOCK);
		return UserEmailConfirmedResponse
				.builder()
				.message(UserEmailConfirmedResponse.EMAIL_CONFIRMED_MESSAGE)
				.build();
	}


	UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
		if (!isValidRequestJsonFormat(userRegistrationRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		User registeredUser = userService.registerUser(userRegistrationRequest);

		if(registeredUser.getRole().equals(Role.MERCHANT) && !registeredUser.getUsername().equals("JohnDoe2")){ //todo delete and
			TemporaryAuthorization temporaryAuthorization = registeredUser.getTemporaryAuthorization();
			emailService.sendConfirmationEmail(registeredUser.getEmail(), temporaryAuthorization.getCode());
		}

		return userService.mapToDto(registeredUser);
	}

	UserDTO addUserAddress(String username, AddressRegisterRequest addressRegisterRequest) {
		if (!isValidAddressRequest(addressRegisterRequest))
			throw new InvalidAddressFormatException(addressRegisterRequest.toString());
		User user = userService.addUserAddress(username, addressRegisterRequest);
		return userService.mapToDto(user);
	}


	UserDTO createAccountForUserWithUsername(String username) {
		User user = userService.getUserByUsername(username);
		Account newAccount = accountService.createAccount(user.getAddress().getCountry());
		userService.addAccountToUser(username, newAccount);

		return userService.mapToDto(user);
	}

	UserDTO createCreditCardForUserWithUsername(String username) {
		User user = userService.getUserByUsername(username);
		CreditCard newCreditCard = creditCardService.createCreditCard(user.getAccount().getCountry());
		userService.addCreditCardToUser(username, newCreditCard);
		emailService.sendCreditCardPin(user.getEmail(), newCreditCard.getPin());
		return userService.mapToDto(user);
	}

	UserDTO getUserByUsername(String username) {
		User user = userService.getUserByUsername(username);
		return userService.mapToDto(user);
	}

	List<UserDTO> getAllRegisteredUsers() {
		return userService.getAllRegisteredUsers();
	}

	UserDeleteResponse deleteUserByUsername(String username) {
		User user = userService.getUserByUsername(username);

		if(user.isHasAnyCreditCard())
			creditCardService.deleteCreditCardsFromAccountByUsername(username);

		return userService.deleteUserByUsername(username);
	}

	UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
		User user = userService.changeRole(updateRequest.username(), updateRequest.role());
		return userService.mapToDto(user);
	}

	UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		if (!isValidChangeLockRequest(updateRequest))
			throw new RequestValidationException(String.format(WRONG_JSON_FORMAT));
		return userService.changeLock(updateRequest.username(), updateRequest.operation());
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

	private boolean isValidConfirmEmailRequest(ConfirmEmailRequest updateRequest) {
		return Stream.of(updateRequest.code(),
						updateRequest.username())
				.noneMatch(s -> s == null || s.isEmpty());
	}

}
