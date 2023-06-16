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
		int lastDigit = calculateLuhnDigit(cardNumberBuilder.toString());
		cardNumberBuilder.append(lastDigit);

		return cardNumberBuilder.toString();
	}

	private static int calculateLuhnDigit(String cardNumber) {
		int[] digits = new int[cardNumber.length()];
		for (int i = 0; i < cardNumber.length(); i++) {
			digits[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
		}

		for (int i = digits.length - 2; i >= 0; i -= 2) {
			int doubledDigit = digits[i] * 2;
			if (doubledDigit > 9) {
				doubledDigit = doubledDigit % 10 + 1;
			}
			digits[i] = doubledDigit;
		}

		int sum = 0;
		for (int digit : digits) {
			sum += digit;
		}

		int checksum = 10 - (sum % 10);
		return (checksum == 10) ? 0 : checksum;
	}
}
