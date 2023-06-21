package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedCreditCardAssignmentException extends DuplicateResourceException {
	public DuplicatedCreditCardAssignmentException(String number) {
		super(String.format("Credit card with number %s is already assigned!", number));
	}
}
