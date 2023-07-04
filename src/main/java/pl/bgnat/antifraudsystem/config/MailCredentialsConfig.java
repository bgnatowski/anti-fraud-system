package pl.bgnat.antifraudsystem.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter

@Configuration
@PropertySource("classpath:mail_credentials.yml")
public class MailCredentialsConfig {
	@Value("${host}")
	private String host;
	@Value("${port}")
	private int port;
	@Value("${email}")
	private String fromEmail;
	@Value("${appPassword}")
	private String appPassword;
	@Value("${properties}")
	private Properties properties;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		// Konfiguracja JavaMailSender...
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(fromEmail);
		mailSender.setPassword(appPassword);
		mailSender.setJavaMailProperties(properties);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		props.put("mail.from.email", fromEmail);
		return mailSender;
	}



}
