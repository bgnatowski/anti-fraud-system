package pl.bgnat.antifraudsystem.bank.user.domain;

import pl.bgnat.antifraudsystem.bank.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;

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

    static User createMerchant(UserRegistrationRequest userRegistrationRequest) {
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
                .build();
    }
}
