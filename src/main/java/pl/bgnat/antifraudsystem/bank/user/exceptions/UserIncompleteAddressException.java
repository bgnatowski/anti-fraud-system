package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserIncompleteAddressException extends RequestValidationException {
	public UserIncompleteAddressException() {
		super("User account is incomplete. You should add address first");
	}
}
