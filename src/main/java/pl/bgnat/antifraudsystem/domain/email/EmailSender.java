package pl.bgnat.antifraudsystem.domain.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.email.config.MailCredentialsConfig;
import pl.bgnat.antifraudsystem.domain.exceptions.SendingEmailException;

@Component
@RequiredArgsConstructor
class EmailSender {
	private final JavaMailSender mailSender;
	private final MailCredentialsConfig mailCredentialsConfig;

	void sendEmail(EmailDTO email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailCredentialsConfig.getFromEmail());
			helper.setTo(email.to());
			helper.setSubject(email.subject());
			helper.setText(email.content());
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new SendingEmailException(e.getMessage());
		}
	}
}
