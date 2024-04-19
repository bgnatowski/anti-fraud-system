package pl.bgnat.antifraudsystem.domain.cards;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CardDataGenerator {
    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int PIN_LENGTH = 4;
    private static final int CVV_LENGTH = 3;
    private static final Random random = new Random();
    private static StringBuilder numberBuilder;

    public static String generateCreditCardNumber() {
        getRandomNumber(CARD_NUMBER_LENGTH - 1);

        // Append the last digit using the Luhn algorithm
        int lastDigit = CardNumberValidator.calculateLuhnDigit(numberBuilder.toString());
        numberBuilder.append(lastDigit);

        return numberBuilder.toString();
    }

    public static String generatePin() {
        return getRandomNumber(PIN_LENGTH);
    }

    public static String generateCvv() {
        return getRandomNumber(CVV_LENGTH);
    }

    @NotNull
    private static String getRandomNumber(int length) {
        numberBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            numberBuilder.append(digit);
        }

        return numberBuilder.toString();
    }
}


