package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUserPhoneNumberException extends DuplicateResourceException {
	public DuplicatedUserPhoneNumberException(String number) {
		super(String.format("Phone number: %s is already assigned to the different user!", number));
	}
}
