package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class CardNumberFormatException extends RequestValidationException {
	public static final String ERROR_MESSAGE_WRONG_CARD_NUMBER_FORMAT_S = "[fieldName: %s , errorMessage: Wrong card number format: %s]";
	public CardNumberFormatException(String number) {
		super(ERROR_MESSAGE_WRONG_CARD_NUMBER_FORMAT_S.formatted("number",number));
	}
}
