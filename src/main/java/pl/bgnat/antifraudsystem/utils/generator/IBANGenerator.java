package pl.bgnat.antifraudsystem.utils.generator;

import java.util.Random;

public class IBANGenerator {
	public static String generateIBAN(String countryCode) {
		StringBuilder ibanBuilder = new StringBuilder();

		// Add the country code and checksum digits
		ibanBuilder.append(countryCode);
		ibanBuilder.append("00");

		// Generate the rest of the digits randomly
		Random random = new Random();
		for (int i = 0; i < 14; i++) {
			int digit = random.nextInt(10);
			ibanBuilder.append(digit);
		}

		return ibanBuilder.toString();
	}
}
