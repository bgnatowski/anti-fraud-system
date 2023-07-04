package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.*;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private UserManager userManager;

	public List<UserDTO> getAllRegisteredUsers() {
		return userManager.getAllRegisteredUsers();
	}

	public UserDTO registerUser(UserRegistrationRequest user) {
		return userManager.registerUser(user);
	}

	public UserEmailConfirmedResponse confirmUserEmail(ConfirmEmailRequest confirmEmailRequest) {
		return userManager.confirmUserEmail(confirmEmailRequest);
	}

	public UserDTO addUserAddress(String username, AddressRegisterRequest addressRegisterRequest) {
		return userManager.addUserAddress(username, addressRegisterRequest);
	}

	public UserDTO createCreditCardForUserWithUsername(String username) {
		return userManager.createCreditCardForUserWithUsername(username);
	}

	public UserDTO createAccountForUserWithUsername(String username) {
		return userManager.createAccountForUserWithUsername(username);
	}

	public UserDTO getUserByUsername(String username) {
		return userManager.getUserByUsername(username);
	}

	public UserDeleteResponse deleteUserByUsername(String username) {
		return userManager.deleteUserByUsername(username);
	}

	public UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
		return userManager.changeRole(updateRequest);
	}

	public UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
		return userManager.changeLock(updateRequest);
	}
}
