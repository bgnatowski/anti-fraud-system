package pl.bgnat.antifraudsystem.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;

@Component
public class UserCreator {
	static User createAdministrator(UserRegistrationRequest userRegistrationRequest, PasswordEncoder passwordEncoder) {
		return new User(userRegistrationRequest.name(),
				userRegistrationRequest.username(),
				passwordEncoder.encode(userRegistrationRequest.password()),
				Role.ADMINISTRATOR,
				true);
	}

	static User createMerchant(UserRegistrationRequest userRegistrationRequest, PasswordEncoder passwordEncoder) {
		User user;
		user = new User(userRegistrationRequest.name(),
				userRegistrationRequest.username(),
				passwordEncoder.encode(userRegistrationRequest.password()),
				Role.MERCHANT,
				false);
		return user;
	}
}
