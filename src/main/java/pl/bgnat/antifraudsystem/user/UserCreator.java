package pl.bgnat.antifraudsystem.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.enums.Role;

@Component
class UserCreator {
	private final PasswordEncoder passwordEncoder;

	UserCreator(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	User createAdministrator(UserRegistrationRequest userRegistrationRequest) {
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
	User createMerchant(UserRegistrationRequest userRegistrationRequest) {
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
