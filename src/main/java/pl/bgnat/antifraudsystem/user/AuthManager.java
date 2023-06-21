package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.exceptions.InvalidConfirmationCode;
import pl.bgnat.antifraudsystem.user.exceptions.TemporaryAuthorizationException;
import pl.bgnat.antifraudsystem.user.exceptions.UserIsAlreadyUnlockException;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class AuthManager {
	private final UserService userService;
	private final CreditCardService creditCardService;
	private final AccountService accountService;
	private final Clock clock;

	UserEmailConfirmedResponse confirmUserEmail(String username, String code) {
		UserDTO user = userService.getUserByUsername(username);

		LocalDateTime now = LocalDateTime.now(clock);
		if (user.isActive())
			throw new UserIsAlreadyUnlockException(username, user.email());

		TemporaryAuthorizationDTO temporaryAuthorizationDTO = user.temporaryAuthorization();
		LocalDateTime expirationDate = temporaryAuthorizationDTO.expirationDate();

		if (expirationDate.isBefore(now))
			throw new TemporaryAuthorizationException(username);
		if (!temporaryAuthorizationDTO.code().equals(code))
			throw new InvalidConfirmationCode(code);

		changeLock(new UserUnlockRequest(username, "UNLOCK"));

		return UserEmailConfirmedResponse
				.builder()
				.message("User email confirmed")
				.build();
	}

	UserDTO registerUser(UserRegistrationRequest user) {
		return userService.registerUser(user);
	}

	UserDTO addUserAddress(String username, AddressRegisterRequest addressRegisterRequest) {
		return userService.addUserAddress(username, addressRegisterRequest);
	}


	UserDTO addCreditCardToUser(String username) {
		CreditCard newCreditCard = creditCardService.createCreditCard();
		return userService.addCreditCardToUser(username,newCreditCard);
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
		return userService.changeLock(updateRequest);
	}
}
