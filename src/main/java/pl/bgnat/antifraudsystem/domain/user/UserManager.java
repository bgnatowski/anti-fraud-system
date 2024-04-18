package pl.bgnat.antifraudsystem.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.account.AccountDTO;
import pl.bgnat.antifraudsystem.domain.account.AccountFacade;
import pl.bgnat.antifraudsystem.domain.address.Address;
import pl.bgnat.antifraudsystem.domain.address.AddressFacade;
import pl.bgnat.antifraudsystem.domain.cards.creditcard.CreditCard;
import pl.bgnat.antifraudsystem.domain.cards.creditcard.CreditCardDTO;
import pl.bgnat.antifraudsystem.domain.cards.creditcard.CreditCardFacade;
import pl.bgnat.antifraudsystem.domain.email.EmailFacade;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.exceptions.AdministratorCannotBeLockException;
import pl.bgnat.antifraudsystem.domain.exceptions.IllegalChangeLockOperationException;
import pl.bgnat.antifraudsystem.domain.request.*;
import pl.bgnat.antifraudsystem.domain.response.*;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorizationFacade;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
class UserManager {
    private final UserService userService;
    private final EmailFacade emailFacade;
    private final CreditCardFacade creditCardFacade;
    private final AccountFacade accountFacade;
    private final AddressFacade addressFacade;
    private final TemporaryAuthorizationFacade temporaryAuthorizationFacade;

    @Transactional
    UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
        TemporaryAuthorization temporaryAuthorization = temporaryAuthorizationFacade.createTemporaryAuthorization();
        User registeredUser = userService.registerUser(userRegistrationRequest, temporaryAuthorization);
        if(isMerchant(registeredUser)){
            emailFacade.sendConfirmationEmail(registeredUser.getEmail(), temporaryAuthorization.getCode());
        }
        return UserDTO.MAPPER.map(registeredUser);
    }

    UserDTO getUserByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return UserDTO.MAPPER.map(user);
    }

    @Transactional
    UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
        User user = userService.getUserByUsername(updateRequest.username());
        Role role = Role.parse(updateRequest.role());
        userService.changeRole(user, role);
        return UserDTO.MAPPER.map(user);
    }

    @Transactional
    UserDTO addAddress(String username, AddressRegisterRequest addressRegisterRequest) {
        User user = userService.getUserByUsername(username);
        Address userAddress = addressFacade.assignAddress(user, addressRegisterRequest);
        user.setAddress(userAddress);
//        User updatedUser = userService.saveOrUpdateUser(user);
        return UserDTO.MAPPER.map(user);
    }

    List<UserDTO> getAllRegisteredUsers() {
        return userService.getAllRegisteredUsers()
                .stream()
                .map(UserDTO.MAPPER)
                .sorted(Comparator.comparingLong(UserDTO::id))
                .toList();
    }

    @Transactional
    UserEmailConfirmedResponse confirmUserEmail(ConfirmEmailRequest confirmEmailRequest) {
        User user = userService.getUserByUsername(confirmEmailRequest.username());

        if (user.isAccountNonLocked())
            return UserEmailConfirmedResponse
                    .builder()
                    .message(UserEmailConfirmedResponse.EMAIL_ALREADY_CONFIRMED_MESSAGE)
                    .build();

        String username = confirmEmailRequest.username();
        String code = confirmEmailRequest.code();

        TemporaryAuthorization userTemporaryAuthorization =
                temporaryAuthorizationFacade.getTemporaryAuthorization(username);

        emailFacade.confirmEmail(userTemporaryAuthorization, code);
        user.unlockAccount();

        return UserEmailConfirmedResponse
                .builder()
                .message(UserEmailConfirmedResponse.EMAIL_CONFIRMED_MESSAGE)
                .build();
    }

    @Transactional
    String regenerateCodeForUser(String username) {
        User user = userService.getUserByUsername(username);
        if(user.isAccountNonLocked())
            return "Cannot regenerate code. Already activated user";

        TemporaryAuthorization tempAuth = temporaryAuthorizationFacade.createTemporaryAuthorization();
        tempAuth.setUser(user);
        user.setTemporaryAuthorization(tempAuth);
        emailFacade.sendConfirmationEmail(user.getEmail(), tempAuth.getCode());

        return "Code has been sent for user: %s".formatted(user.getEmail());
    }

    @Transactional
    UserAccountCreatedResponse createAccountForUserWithUsername(String username) {
        User user = userService.getUserByUsername(username);
        Account newAccount = accountFacade.createAccount(user);
        userService.validUserForAccount(user);

        user.setAccount(newAccount);
        newAccount.setOwner(user);
        user.setHasAccount(true);

        return UserAccountCreatedResponse.builder()
                .userDTO(UserDTO.MAPPER.map(user))
                .accountDTO(AccountDTO.MAPPER.map(user.getAccount()))
                .build();
    }

    @Transactional
    UserCreditCardCreatedResponse createCreditCardForUserWithUsername(String username) {
        User user = userService.getUserByUsername(username);
        CreditCard newCreditCard = creditCardFacade.createCreditCardForUser(user);

        userService.validUserForCreditCard(user);

        newCreditCard.setAccount(user.getAccount());
        newCreditCard.setCountry(user.getAccount().getCountry());
//        user.getAccount().getCreditCards();
//        if(user.getAccount().getCreditCards()==null){
//            user.getAccount().setCre
//        }

        if (!user.isHasAnyCreditCard())
            user.setHasAnyCreditCard(true);
        user.increaseNumberOfCreditCards();

        emailFacade.sendCreditCardPin(user.getEmail(), newCreditCard.getPin());

        return UserCreditCardCreatedResponse.builder()
                .userDTO(UserDTO.MAPPER.map(user))
                .accountDTO(AccountDTO.MAPPER.map(user.getAccount()))
                .creditCardDTO(CreditCardDTO.MAPPER.map(newCreditCard))
                .build();
    }

    @Transactional
    UserDeleteResponse deleteUserByUsername(String username) {
        User user = userService.getUserByUsername(username);

        if (user.isHasAnyCreditCard())
            creditCardFacade.deleteCreditCardsFromAccountByUsername(username);
        if (user.isHasAccount())
            accountFacade.deleteAccountForUser(username);

        userService.deleteUserByUsername(username);

        return UserDeleteResponse.builder()
                .username(username)
                .status(UserDeleteResponse.DELETED_SUCCESSFULLY_RESPONSE)
                .build();
    }

    @Transactional
    UserUnlockResponse changeLock(UserUnlockRequest updateRequest) {
        User user = userService.getUserByUsername(updateRequest.username());
        String operation = updateRequest.operation();
        boolean isLockOperation = UserUnlockRequest.LOCK.equals(operation);
        UserUnlockResponse.UserUnlockResponseBuilder responseBuilder = UserUnlockResponse.builder();

        if (isAdministrator(user) && isLockOperation)
            throw new AdministratorCannotBeLockException();
        if (user.isAccountNonLocked() && isLockOperation) {
            user.lockAccount();
            responseBuilder.status(String.format(UserUnlockResponse.MESSAGE_PATTERN, user.getUsername(), "locked!"));
        } else if (!user.isAccountNonLocked() && !isLockOperation) {
            user.unlockAccount();
            responseBuilder.status(String.format(UserUnlockResponse.MESSAGE_PATTERN, user.getUsername(), "unlocked!"));
        } else {
            throw new IllegalChangeLockOperationException(operation);
        }
        return responseBuilder.build();
    }

    private boolean isAdministrator(User user) {
        return Role.ADMINISTRATOR.equals(user.getRole());
    }

    private boolean isMerchant(User user) {
        return Role.MERCHANT.equals(user.getRole());
    }
}
