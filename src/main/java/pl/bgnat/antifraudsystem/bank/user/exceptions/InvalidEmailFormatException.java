package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidEmailFormatException extends RequestValidationException {

	public static final String INVALID_EMAIL_FORMAT_S_PATTERN = "Invalid email format: %s";

	public InvalidEmailFormatException(String email) {
		super(String.format(INVALID_EMAIL_FORMAT_S_PATTERN, email));

	}
}
