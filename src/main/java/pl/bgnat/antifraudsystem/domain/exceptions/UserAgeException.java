package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserAgeException extends RequestValidationException {
	public UserAgeException() {
		super("Age of majority required");
	}
}
