package pl.bgnat.antifraudsystem.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.exceptions.*;
import pl.bgnat.antifraudsystem.domain.phone.PhoneNumber;
import pl.bgnat.antifraudsystem.domain.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    User registerUser(UserRegistrationRequest userRegistrationRequest, TemporaryAuthorization temporaryAuthorization) {
        validRegistration(userRegistrationRequest);

        User createdUser = createProperUser(userRegistrationRequest, temporaryAuthorization);
        createdUser.setPassword(passwordEncoder.encode(userRegistrationRequest.password()));

        PhoneNumber userPhone = PhoneNumber.builder()
                .user(createdUser)
                .number(userRegistrationRequest.phoneNumber())
                .build();
        createdUser.setPhone(userPhone);

        createdUser = userRepository.save(createdUser);
        temporaryAuthorization.setUser(createdUser);
        return createdUser;
    }

    User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    List<User> getAllRegisteredUsers() {
        Page<User> page = userRepository.findAll(Pageable.ofSize(100));
        return page.getContent();
    }

    void validUserForAccount(User user) {
        validUserProfile(user);
        validAccountNonExist(user);
    }

    void validUserForCreditCard(User user) {
        validUserProfile(user);
        validAccountExists(user.getAccount());
    }

    void changeRole(User user, Role role) {
        validChangeRole(role, user);
        user.setRole(role);
    }

    void deleteUserByUsername(String username) {
        validUserExistsByUsername(username);
        userRepository.deleteUserByUsername(username);
    }

    private User createProperUser(UserRegistrationRequest userRegistrationRequest, TemporaryAuthorization temporaryAuthorization) {
        if (!userRepository.existsUserByRole(Role.ADMINISTRATOR))
            return UserCreator.createAdministrator(userRegistrationRequest);
        return UserCreator.createMerchant(userRegistrationRequest, temporaryAuthorization);
    }

    private void validRegistration(UserRegistrationRequest userRegistrationRequest) {
        validEmail(userRegistrationRequest.email());
        validUsername(userRegistrationRequest.username());
        validPhoneNumber(userRegistrationRequest.phoneNumber());
        validDateOfBirth(userRegistrationRequest.dateOfBirth());
    }

    private void validUserProfile(User user) {
        if (!user.isAccountNonLocked())
            throw new UserLockedException();
        if (user.getAddress() == null)
            throw new UserIncompleteAddressException();
    }

    private void validPhoneNumber(String phoneNumber) {
        if (userRepository.existsUserByPhone_Number(phoneNumber))
            throw new DuplicatedUserPhoneNumberException(phoneNumber);
    }

    private void validUsername(String username) {
        if (userRepository.existsUserByUsername(username))
            throw new DuplicatedUserUsernameException(username);
    }

    private void validEmail(String email) {
        if (userRepository.existsUserByEmail(email))
            throw new DuplicatedUserEmailException(email);
    }

    private void validChangeRole(Role role, User user) {
        if (!isSupportOrMerchant(role))
            throw new AdministratorRoleCannotBeChangedException();
        if (isTheSameRoleAlreadyAssigned(role, user))
            throw new DuplicatedUserRoleException(user.getRole().name(), user.getUsername());
    }

    private void validDateOfBirth(LocalDate dateOfBirth) {
        if (!DateTimeUtils.isAdult(dateOfBirth, DateTimeUtils.currentLocalDate()))
            throw new UserAgeException();
    }

    private void validAccountExists(Account account) {
        if (account == null)
            throw new CreditCardWithoutAccountException();
    }

    private void validAccountNonExist(User user) {
        if (user.getAccount() != null && user.isHasAccount())
            throw new DuplicatedAccountAssignmentException(user.getAccount().toString());
    }

    private void validUserExistsByUsername(String username) {
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
