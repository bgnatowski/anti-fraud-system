package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class CreditCardAlreadyAssignedException extends DuplicateResourceException {
	public CreditCardAlreadyAssignedException(String number) {
		super(String.format("Credit card with number %s is already assigned!", number));
	}
}
