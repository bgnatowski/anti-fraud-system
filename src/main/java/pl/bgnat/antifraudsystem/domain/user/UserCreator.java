package pl.bgnat.antifraudsystem.domain.user;

import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.domain.tempauth.TemporaryAuthorization;

class UserCreator {
    static User createAdministrator(UserRegistrationRequest userRegistrationRequest) {
        return User.builder()
                .firstName(userRegistrationRequest.firstName())
                .lastName(userRegistrationRequest.lastName())
                .username(userRegistrationRequest.username())
                .email(userRegistrationRequest.email())
                .dateOfBirth(userRegistrationRequest.dateOfBirth())
                .role(Role.ADMINISTRATOR)
                .accountNonLocked(true)
                .hasAccount(false)
                .hasAnyCreditCard(false)
                .build();
    }

    static User createMerchant(UserRegistrationRequest userRegistrationRequest, TemporaryAuthorization temporaryAuthorization) {
        return User.builder()
                .firstName(userRegistrationRequest.firstName())
                .lastName(userRegistrationRequest.lastName())
                .username(userRegistrationRequest.username())
                .email(userRegistrationRequest.email())
                .dateOfBirth(userRegistrationRequest.dateOfBirth())
                .role(Role.MERCHANT)
                .accountNonLocked(false)
                .hasAccount(false)
                .hasAnyCreditCard(false)
                .temporaryAuthorization(temporaryAuthorization)
                .build();
    }
}
