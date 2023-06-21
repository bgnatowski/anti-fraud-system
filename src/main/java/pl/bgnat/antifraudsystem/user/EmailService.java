package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.exceptions.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class EmailService {
	private final EmailSender emailSender;
	private final Clock clock;

	void sendConfirmationEmail(UserDTO userDTO){
		String email = userDTO.email();
		String code = userDTO.temporaryAuthorization().code();

		emailSender.sendEmail(email, code);
	}

	void confirmEmail(UserDTO user, String code) {
		LocalDateTime now = LocalDateTime.now(clock);
		String username = user.username();
		String confirmationCode = user.temporaryAuthorization().code();
		LocalDateTime expirationDate = user.temporaryAuthorization().expirationDate();

		if (user.isActive())
			throw new UserIsAlreadyUnlockException(username, user.email());
		if (expirationDate.isBefore(now))
			throw new TemporaryAuthorizationExpiredException(username);
		if (!code.equals(confirmationCode))
			throw new InvalidConfirmationCodeException(code);
	}
}
