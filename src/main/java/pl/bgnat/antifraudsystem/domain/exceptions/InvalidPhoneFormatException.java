package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidPhoneFormatException extends RequestValidationException {

	public static final String INVALID_PHONE_NUMBER_FORMAT_S = "Invalid phone number format: %s";

	public InvalidPhoneFormatException(String number) {
		super(String.format(INVALID_PHONE_NUMBER_FORMAT_S, number));
	}
}
