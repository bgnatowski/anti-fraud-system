package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserManager {
	private final UserService userService;
	private final EmailService emailService;
	private final CreditCardService creditCardService;
	private final AccountService accountService;

	UserEmailConfirmedResponse confirmUserEmail(String username, String code) {
		UserDTO user = userService.getUserByUsername(username);
		emailService.confirmEmail(user, code);
		changeLock(new UserUnlockRequest(username, "UNLOCK"));
		return UserEmailConfirmedResponse
				.builder()
				.message("User email confirmed")
				.build();
	}

	UserDTO registerUser(UserRegistrationRequest user) {



		String confirmationCode = emailService.getConfirmationCode(user.email());
		UserDTO userDTO = userService.registerUser(user, confirmationCode);
		emailService.sendConfirmationEmail(userDTO.email(), confirmationCode);
		return userDTO;
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
