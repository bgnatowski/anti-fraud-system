package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.EmailDTO;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.user.exceptions.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class EmailService {
	private final EmailSender emailSender;
	private final Clock clock;

	void sendConfirmationEmail(String emailTo, String code){
		EmailDTO confirmationEmail = EmailDTO.builder()
				.to(emailTo)
				.subject("[PiggsyBank] Potwierdzenie rejestracji!")
				.content("Witaj! Dziękujemy za rejestrację. Kod potwierdzający: " + code)
				.build();
		emailSender.sendEmail(confirmationEmail);
	}

	void sendCreditCardPin(String emailTo, String pin) {
		EmailDTO emailWithCreditCardPin = EmailDTO.builder()
				.to(emailTo)
				.subject("[PiggsyBank] Pin aktywacyjny")
				.content("Witaj! Dziękujemy za utworzenie karty. \nOto twój pin aktywacyjny: " + pin)
				.build();
		emailSender.sendEmail(emailWithCreditCardPin);
	}

	void confirmEmail(TemporaryAuthorization temporaryAuthorization, String code) {
		LocalDateTime now = LocalDateTime.now(clock);
		String username = temporaryAuthorization.getUser().getUsername();
		String confirmationCode = temporaryAuthorization.getCode();
		LocalDateTime expirationDate = temporaryAuthorization.getExpirationDate();

		if (expirationDate.isBefore(now))
			throw new TemporaryAuthorizationExpiredException(username);
		if (!code.equals(confirmationCode))
			throw new InvalidConfirmationCodeException(code);
	}
}
