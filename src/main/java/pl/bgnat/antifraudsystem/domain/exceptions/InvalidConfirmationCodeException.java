package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidConfirmationCodeException extends RequestValidationException {

	public static final String INVALID_CONFIRMATION_CODE_S_PATTERN = "Invalid confirmation code %s";

	public InvalidConfirmationCodeException(String code) {
		super(String.format(INVALID_CONFIRMATION_CODE_S_PATTERN, code));
	}
}
