package pl.bgnat.antifraudsystem.bank.transaction.stolenCards.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class CardNumberFormatException extends RequestValidationException {
	public static final String  WRONG_CARD_NUMBER_FORMAT_S = "Wrong card number format: %s";
	public CardNumberFormatException(String number) {
		super(String.format(WRONG_CARD_NUMBER_FORMAT_S, number));
	}
}
