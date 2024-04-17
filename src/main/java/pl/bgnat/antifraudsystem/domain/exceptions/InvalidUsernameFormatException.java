package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidUsernameFormatException extends RequestValidationException {
	public InvalidUsernameFormatException(String username) {
		super(String.format("Wpisany: '%s' zawiera niedozwolone znaki", username));
	}
}
