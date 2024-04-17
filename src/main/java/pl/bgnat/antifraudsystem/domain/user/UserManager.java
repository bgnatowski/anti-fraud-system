package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.account.AccountFacade;
import pl.bgnat.antifraudsystem.domain.address.Address;
import pl.bgnat.antifraudsystem.domain.address.AddressFacade;
import pl.bgnat.antifraudsystem.domain.creditcard.CreditCard;
import pl.bgnat.antifraudsystem.domain.creditcard.CreditCardFacade;
import pl.bgnat.antifraudsystem.domain.email.EmailFacade;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.exceptions.InvalidAddressFormatException;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorizationFacade;
import pl.bgnat.antifraudsystem.dto.UserDTO;
import pl.bgnat.antifraudsystem.dto.request.*;
import pl.bgnat.antifraudsystem.dto.response.*;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
@RequiredArgsConstructor
class UserManager {
    private final UserService userService;
    private final EmailFacade emailFacade;
    private final CreditCardFacade creditCardFacade;
    private final AccountFacade accountFacade;
    private final AddressFacade addressFacade;
    private final TemporaryAuthorizationFacade temporaryAuthorizationFacade;

    UserDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
        if (!isValidRequestJsonFormat(userRegistrationRequest))
            throw new RequestValidationException(WRONG_JSON_FORMAT);

        User registeredUser = userService.registerUser(userRegistrationRequest);

        if (registeredUser.getRole().equals(Role.MERCHANT) && !registeredUser.getUsername().equals("JohnDoe2")) { //todo delete and
            TemporaryAuthorization temporaryAuthorization = registeredUser.getTemporaryAuthorization();
            emailFacade.sendConfirmationEmail(registeredUser.getEmail(), temporaryAuthorization.getCode());
        }

        return userService.mapToDto(registeredUser);
    }

    UserDTO getUserByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return userService.mapToDto(user);
    }

    UserDTO changeRole(UserUpdateRoleRequest updateRequest) {
        User user = userService.changeRole(updateRequest.username(), updateRequest.role());
        return userService.mapToDto(user);
    }

    List<UserDTO> getAllRegisteredUsers() {
        return userService.getAllRegisteredUsers();
    }

    UserEmailConfirmedResponse confirmUserEmail(ConfirmEmailRequest confirmEmailRequest) {
        if (!isValidConfirmEmailRequest(confirmEmailRequest))
            throw new RequestValidationException(WRONG_JSON_FORMAT);

        String username = confirmEmailRequest.username();
        String code = confirmEmailRequest.code();

        TemporaryAuthorization userTemporaryAuthorization =
                temporaryAuthorizationFacade.getTemporaryAuthorization(username);

        emailFacade.confirmEmail(userTemporaryAuthorization, code);
        userService.changeLock(username, UserUnlockRequest.UNLOCK);

        return UserEmailConfirmedResponse
                .builder()
                .message(UserEmailConfirmedResponse.EMAIL_CONFIRMED_MESSAGE)
                .build();
    }

    UserWithAddressResponse addAddress(String username, AddressRegisterRequest addressRegisterRequest) {
        if (!isValidAddressRequest(addressRegisterRequest))
            throw new InvalidAddressFormatException(addressRegisterRequest.toString());

        User user = userService.getUserByUsername(username);
        Address userAddress = addressFacade.assignAddress(user, addressRegisterRequest);
        user.setAddress(userAddress);
        User updatedUser = userService.updateUser(user);

        return UserWithAddressResponse.builder()
                .userDTO(userService.mapToDto(updatedUser))
                .addressDTO(addressFacade.mapToDto(userAddress))
                .build();
    }

    UserAccountCreatedResponse createAccountForUserWithUsername(String username) {
        User user = userService.getUserByUsername(username);
        Account newAccount = accountFacade.createAccount(user.getAddress().getCountry());
        user = userService.addAccountToUser(username, newAccount);
        user = userService.updateUser(user);

        return UserAccountCreatedResponse.builder()
                .userDTO(userService.mapToDto(user))
                .accountDTO(accountFacade.mapToDto(newAccount))
                .build();
    }

    UserCreditCardCreatedResponse createCreditCardForUserWithUsername(String username) {
        User user = userService.getUserByUsername(username);
        CreditCard newCreditCard = creditCardFacade.createCreditCard(user.getAccount().getCountry());
        user = userService.addCreditCardToUser(username, newCreditCard);
        user = userService.updateUser(user);

        emailFacade.sendCreditCardPin(user.getEmail(), newCreditCard.getPin());

        return UserCreditCardCreatedResponse.builder()
                .userDTO(userService.mapToDto(user))
                .accountDTO(accountFacade.mapToDto(user.getAccount()))
                .creditCardDTO(creditCardFacade.mapToDto(newCreditCard))
                .build();
    }

    UserDeleteResponse deleteUserByUsername(String username) {
        User user = userService.getUserByUsername(username);

        if (user.isHasAnyCreditCard())
            creditCardFacade.deleteCreditCardsFromAccountByUsername(username);
        if (user.isHasAccount())
            accountFacade.deleteAccountForUser(username);

        return userService.deleteUserByUsername(username);
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
