package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class UserLockedException extends RequestValidationException {
	public UserLockedException() {
		super("User is locked");
	}
}
