package pl.bgnat.antifraudsystem.domain.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.cards.creditcard.CreditCardDTO;

@Builder
public record CreditCardActionResponse(CreditCardDTO creditCardDTO, String message) {
    private static final String CREDIT_CARD_ACTIVATED_MESSAGE = "Credit card activated";
    private static final String CREDIT_CARD_RESTRICT_MESSAGE = "Credit card with number=%s restricted";
    private static final String CREDIT_CARD_WRONG_PIN_ATTEMPT_MESSAGE = "[Security] Wrong pin! You have %d more attempts.";
    private static final String CREDIT_CARD_WRONG_PIN_MESSAGE = "[Security] Wrong pin!";
    private static final String CREDIT_CARD_DELETE_MESSAGE = "Credit card with number=%s deleted";

    public static String getRestrictionMessage(String cardNumber) {
        return CREDIT_CARD_RESTRICT_MESSAGE.formatted(cardNumber);
    }
    public static String getDeletionMessage(String cardNumber) {
        return CREDIT_CARD_DELETE_MESSAGE.formatted(cardNumber);
    }
    public static String getWrongPinWithAttemptsMessage(int attempts) {
        return CREDIT_CARD_WRONG_PIN_ATTEMPT_MESSAGE.formatted(attempts);
    }

    public static String getActivationMessage(){
        return CREDIT_CARD_ACTIVATED_MESSAGE;
    }
    public static String getWrongPinMessage() {
        return CREDIT_CARD_WRONG_PIN_MESSAGE;
    }


}
