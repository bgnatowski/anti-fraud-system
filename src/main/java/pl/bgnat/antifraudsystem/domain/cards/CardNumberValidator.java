package pl.bgnat.antifraudsystem.domain.cards;

public class CardNumberValidator {
    private static final int CHECKSUM_DIGIT_POSITION = 15;
    private static final int CARD_NUMBER_LENGTH = 16;

    public static boolean isValid(String number) {
        if (number.length() < CARD_NUMBER_LENGTH) return false;
        boolean isOddPosition = true;
        int sum = 0;
        int checkSum = Integer.parseInt(Character.toString((number.charAt(CHECKSUM_DIGIT_POSITION))));

        for (int i = 0; i < number.length() - 1; i++) {
            int digit = Integer.parseInt(Character.toString((number.charAt(i))));
            int multipliedOddPositionDigitBy2 = (isOddPosition ? 2 : 1) * digit;
            int substracted9FromOver9Number = multipliedOddPositionDigitBy2 - (multipliedOddPositionDigitBy2 > 9 ? 9 : 0);
            sum += substracted9FromOver9Number;
            isOddPosition = !isOddPosition;
        }
        return (sum + checkSum) % 10 == 0;
    }

    public static int calculateLuhnDigit(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        int checksum = (sum % 10 == 0) ? 0 : 10 - (sum % 10);
        return checksum;
    }
}
