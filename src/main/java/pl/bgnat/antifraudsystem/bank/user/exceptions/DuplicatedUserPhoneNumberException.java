package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUserPhoneNumberException extends DuplicateResourceException {
	public DuplicatedUserPhoneNumberException(String number) {
		super(String.format("Phone number: %s is already assigned to the different user!", number));
	}
}
