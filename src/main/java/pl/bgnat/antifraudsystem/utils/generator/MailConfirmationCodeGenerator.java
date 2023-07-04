package pl.bgnat.antifraudsystem.utils.generator;

import java.util.Random;

public class MailConfirmationCodeGenerator {

	public static String generateConfirmationCode() {
		Random random = new Random();
		int code = random.nextInt(90000) + 10000;
		return String.valueOf(code);
	}
}
