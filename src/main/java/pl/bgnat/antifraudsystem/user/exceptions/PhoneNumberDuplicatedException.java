package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class PhoneNumberDuplicatedException extends DuplicateResourceException {
	public PhoneNumberDuplicatedException(String number) {
		super(String.format("Phone number: %s is already assigned to the different user!", number));
	}
}
