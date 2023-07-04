package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserAgeException extends RequestValidationException {
	public UserAgeException() {
		super("Age of majority required");
	}
}
