package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserLockedException extends RequestValidationException {
	public UserLockedException() {
		super("User is locked");
	}
}
