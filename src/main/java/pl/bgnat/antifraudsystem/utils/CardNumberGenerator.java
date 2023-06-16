package pl.bgnat.antifraudsystem.utils;

import java.util.Random;

public class CardNumberGenerator {
	private static final int CARD_NUMBER_LENGTH = 16;
	public static String generateCreditCardNumber() {
		StringBuilder cardNumberBuilder = new StringBuilder();
		Random random = new Random();

		// Generate the first 15 digits of the card number (excluding the last digit)
		for (int i = 0; i < CARD_NUMBER_LENGTH - 1; i++) {
			int digit = random.nextInt(10);
			cardNumberBuilder.append(digit);
		}

		// Append the last digit using the Luhn algorithm
		int lastDigit = CardNumberValidator.calculateLuhnDigit(cardNumberBuilder.toString());
		cardNumberBuilder.append(lastDigit);

		return cardNumberBuilder.toString();
	}

}
