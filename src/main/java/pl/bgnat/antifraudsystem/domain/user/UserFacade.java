package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.request.*;
import pl.bgnat.antifraudsystem.domain.response.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserManager userManager;

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
        return userManager.addAddress(username, addressRegisterRequest);
    }

    public UserCreditCardCreatedResponse createCreditCardForUserWithUsername(String username) {
        return userManager.createCreditCardForUserWithUsername(username);
    }

    public UserAccountCreatedResponse createAccountForUserWithUsername(String username) {
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
