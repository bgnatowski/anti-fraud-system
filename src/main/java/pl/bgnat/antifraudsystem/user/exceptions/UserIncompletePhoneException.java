package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserIncompletePhoneException extends RequestValidationException {
	public UserIncompletePhoneException() {
		super("User account is incomplete. You should add phone number first");
	}
}
