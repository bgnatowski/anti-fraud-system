package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedAccountAssignmentException extends DuplicateResourceException {
	public DuplicatedAccountAssignmentException(String string) {
		super("User already has an account assigned");
	}
}
