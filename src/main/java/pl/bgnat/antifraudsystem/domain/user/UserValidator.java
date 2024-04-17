package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.exceptions.*;
import pl.bgnat.antifraudsystem.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;
import pl.bgnat.antifraudsystem.utils.validator.BirthDateValidator;
import pl.bgnat.antifraudsystem.utils.validator.EmailValidator;
import pl.bgnat.antifraudsystem.utils.validator.PhoneNumberValidator;
import pl.bgnat.antifraudsystem.utils.validator.UsernameValidator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
class UserValidator {
    private final UserRepository userRepository;

    void validRegistration(UserRegistrationRequest userRegistrationRequest) {
        validEmail(userRegistrationRequest.email());
        validUsername(userRegistrationRequest.username());
        validPhoneNumber(userRegistrationRequest.phoneNumber());
        validDateOfBirth(userRegistrationRequest.dateOfBirth());
    }

    void validUserProfile(User user) {
        if (!user.isAccountNonLocked())
            throw new UserLockedException();
        if (user.getAddress() == null)
            throw new UserIncompleteAddressException();
    }

    void validPhoneNumber(String phoneNumber) {
        if (!PhoneNumberValidator.isValid(phoneNumber))
            throw new InvalidPhoneFormatException(phoneNumber);
        if (userRepository.existsUserByPhone_Number(phoneNumber))
            throw new DuplicatedUserPhoneNumberException(phoneNumber);

    }

    void validUsername(String username) {
        if (userRepository.existsUserByUsername(username))
            throw new DuplicatedUserUsernameException(username);
        if (!UsernameValidator.isValid(username))
            throw new InvalidUsernameFormatException(username);
    }

    void validEmail(String email) {
        if (userRepository.existsUserByEmail(email))
            throw new DuplicatedUserEmailException(email);
        if (!EmailValidator.isValid(email))
            throw new InvalidEmailFormatException(email);
    }

    void validChangeRole(Role role, User user) {
        if (!isSupportOrMerchant(role))
            throw new AdministratorRoleCannotBeChangedException();
        if (isTheSameRoleAlreadyAssigned(role, user))
            throw new DuplicatedUserRoleException(user.getRole().name(), user.getUsername());
    }

    void validDateOfBirth(LocalDate dateOfBirth) {
        if (!BirthDateValidator.isValid(dateOfBirth, DateTimeUtils.currentLocalDate()))
            throw new UserAgeException();
    }

    void validAccountExists(Account account) {
        if (account == null)
            throw new CreditCardWithoutAccountException();
    }

    void validAccountNonExist(User user) {
        if (user.getAccount() != null && user.isHasAccount())
            throw new DuplicatedAccountAssignmentException(user.getAccount().toString());
    }

    void validUserExistsByUsername(String username) {
        if (!userRepository.existsUserByUsername(username))
            throw new UserNotFoundException(username);
    }

    private boolean isTheSameRoleAlreadyAssigned(Role role, User user) {
        return user.getRole().equals(role);
    }

    private boolean isSupportOrMerchant(Role role) {
        return Role.SUPPORT.equals(role) || Role.MERCHANT.equals(role);
    }


}
