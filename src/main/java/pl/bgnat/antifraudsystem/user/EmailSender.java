package pl.bgnat.antifraudsystem.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.config.MailCredentialsConfig;
import pl.bgnat.antifraudsystem.user.exceptions.SendingEmailException;

@Component
@RequiredArgsConstructor
class EmailSender {
	private final JavaMailSender mailSender;
	private final MailCredentialsConfig mailCredentialsConfig;

	void sendEmail(String email, String code) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailCredentialsConfig.getFromEmail());
			helper.setTo(email);
			helper.setSubject("Potwierdzenie rejestracji w PiggsyBank!");
			helper.setText("Witaj! Dziękujemy za rejestrację. Kod potwierdzający: " + code);
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new SendingEmailException(e.getMessage());
		}
	}
}
