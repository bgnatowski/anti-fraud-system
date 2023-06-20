package pl.bgnat.antifraudsystem.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.security.MailCredentialsConfig;
import pl.bgnat.antifraudsystem.user.exceptions.EmailSendingError;
import pl.bgnat.antifraudsystem.user.exceptions.InvalidEmailFormatException;
import pl.bgnat.antifraudsystem.utils.generator.MailConfirmationCodeGenerator;

@Component
public class EmailService {
	private final JavaMailSender mailSender;
	private final MailCredentialsConfig mailCredentialsConfig;
	public EmailService(JavaMailSender mailSender, MailCredentialsConfig mailCredentialsConfig) {
		this.mailSender = mailSender;
		this.mailCredentialsConfig = mailCredentialsConfig;
	}

	public String validateEmail(String email) {
		validEmailFormat(email);
		String confirmationCode = MailConfirmationCodeGenerator.generateConfirmationCode();
		sendConfirmationEmail(email, confirmationCode);
		return confirmationCode;
	}

	private static void validEmailFormat(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		if (!email.matches(emailRegex))
			throw new InvalidEmailFormatException(email);
	}

	private void sendConfirmationEmail(String email, String code) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailCredentialsConfig.getFromEmail());
			helper.setTo(email);
			helper.setSubject("Potwierdzenie rejestracji w PiggsyBank!");
			helper.setText("Witaj! Dziękujemy za rejestrację. Kod potwierdzający: " + code);
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new EmailSendingError(e.getMessage());
		}
	}
}
