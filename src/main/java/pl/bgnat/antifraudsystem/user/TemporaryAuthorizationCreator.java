package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.utils.generator.MailConfirmationCodeGenerator;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
class TemporaryAuthorizationCreator {
	public static final int EXPIRATION_TIME_IN_MINUTES = 15;
	private final Clock clock;
	public TemporaryAuthorization createTemporaryAuthorization(User createdUser) {
		return TemporaryAuthorization.builder()
				.user(createdUser)
				.code(MailConfirmationCodeGenerator.generateConfirmationCode())
				.expirationDate(LocalDateTime.now(clock).plusMinutes(EXPIRATION_TIME_IN_MINUTES))
				.build();
	}
}
