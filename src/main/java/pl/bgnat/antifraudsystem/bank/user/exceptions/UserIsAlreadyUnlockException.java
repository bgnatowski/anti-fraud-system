package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserIsAlreadyUnlockException extends RequestValidationException {
	public UserIsAlreadyUnlockException(String username, String email) {
		super(String.format("User: %s with email: %s is already confirmed", username, email));
	}
}
