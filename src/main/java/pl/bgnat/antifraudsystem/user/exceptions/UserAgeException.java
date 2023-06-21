package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserAgeException extends RequestValidationException {
	public UserAgeException() {
		super("Age of majority required");
	}
}
