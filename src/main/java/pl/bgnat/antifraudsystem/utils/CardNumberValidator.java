package pl.bgnat.antifraudsystem.utils;

import org.springframework.stereotype.Component;

@Component
public class CardNumberValidator {
	private static final int CHECKSUM_DIGIT_POSITION = 15;
	private static final int CARD_NUMBER_LENGTH = 16;
	public static boolean isValid(String number) {
		if (number.length() < CARD_NUMBER_LENGTH) return false;
		int luhnDigit = calculateLuhnDigit(number);
		return luhnDigit == 0;
	}

	public static int calculateLuhnDigit(String number) {
		boolean isOddPosition = true;
		int sum = 0;
		int checkSum = Integer.parseInt(Character.toString((number.charAt(CHECKSUM_DIGIT_POSITION))));

		for(int i = 0; i < number.length()-1; i++){
			int digit = Integer.parseInt(Character.toString((number.charAt(i))));
			int multipliedOddPositionDigitBy2 = (isOddPosition ? 2 : 1) * digit;
			int substracted9FromOver9Number = multipliedOddPositionDigitBy2 - (multipliedOddPositionDigitBy2 > 9 ? 9 : 0);
			sum += substracted9FromOver9Number;
			isOddPosition = !isOddPosition;
		}
		return (sum + checkSum) % 10;
	}
}
