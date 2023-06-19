package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class AccountAlreadyAssignedException extends DuplicateResourceException {
	public AccountAlreadyAssignedException(String string) {
		super("User already has an account assigned");
	}
}
