package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.enums.Role;

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
				.build();
	}
}
