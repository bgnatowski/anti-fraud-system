package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;

@Component
@RequiredArgsConstructor
class UserCreator {
	private final PasswordEncoder passwordEncoder;
	User createAdministrator(UserRegistrationRequest userRegistrationRequest) {
		return User.builder()
				.firstName(userRegistrationRequest.firstName())
				.lastName(userRegistrationRequest.lastName())
				.username(userRegistrationRequest.username())
				.email(userRegistrationRequest.email())
				.password(passwordEncoder.encode(userRegistrationRequest.password()))
				.dateOfBirth(userRegistrationRequest.dateOfBirth())
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.hasAccount(false)
				.hasAnyCreditCard(false)
				.build();
	}
	User createMerchant(UserRegistrationRequest userRegistrationRequest) {
		return User.builder()
				.firstName(userRegistrationRequest.firstName())
				.lastName(userRegistrationRequest.lastName())
				.username(userRegistrationRequest.username())
				.email(userRegistrationRequest.email())
				.password(passwordEncoder.encode(userRegistrationRequest.password()))
				.dateOfBirth(userRegistrationRequest.dateOfBirth())
				.role(Role.MERCHANT)
				.accountNonLocked(false)
				.hasAccount(false)
				.hasAnyCreditCard(false)
				.build();
	}
}
