package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.exceptions.*;
import pl.bgnat.antifraudsystem.utils.generator.MailConfirmationCodeGenerator;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailService {
	private final EmailSender emailSender;
	private final Clock clock;

	public String getConfirmationCode(String email) {
		validEmailFormat(email);
		return MailConfirmationCodeGenerator.generateConfirmationCode();
	}

	public void sendConfirmationEmail(String email, String confirmationCode){
		emailSender.sendEmail(email, confirmationCode);
	}

	private static void validEmailFormat(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		if (!email.matches(emailRegex))
			throw new InvalidEmailFormatException(email);
	}

	public void confirmEmail(UserDTO user, String code) {
		LocalDateTime now = LocalDateTime.now(clock);
		String username = user.username();
		String confirmationCode = user.temporaryAuthorization().code();
		LocalDateTime expirationDate = user.temporaryAuthorization().expirationDate();

		if (user.isActive())
			throw new UserIsAlreadyUnlockException(username, user.email());
		if (expirationDate.isBefore(now))
			throw new TemporaryAuthorizationException(username);
		if (!code.equals(confirmationCode))
			throw new InvalidConfirmationCode(code);
	}
}
