package pl.bgnat.antifraudsystem.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.UserRegistrationRequest;

@Component
class UserCreator {
	static User createAdministrator(UserRegistrationRequest userRegistrationRequest, PasswordEncoder passwordEncoder) {
		return User.builder()
				.firstName(userRegistrationRequest.firstName())
				.lastName(userRegistrationRequest.lastName())
				.username(userRegistrationRequest.username())
				.email(userRegistrationRequest.email())
				.password(passwordEncoder.encode(userRegistrationRequest.password()))
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
	}
	static User createMerchant(UserRegistrationRequest userRegistrationRequest, PasswordEncoder passwordEncoder) {
		return User.builder()
				.firstName(userRegistrationRequest.firstName())
				.lastName(userRegistrationRequest.lastName())
				.username(userRegistrationRequest.username())
				.email(userRegistrationRequest.email())
				.password(passwordEncoder.encode(userRegistrationRequest.password()))
				.role(Role.MERCHANT)
				.accountNonLocked(false)
				.build();
	}
}
