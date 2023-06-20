package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidEmailFormatException extends RequestValidationException {
	public InvalidEmailFormatException(String email) {
		super(String.format("Invalid email format: %s", email));

	}
}
