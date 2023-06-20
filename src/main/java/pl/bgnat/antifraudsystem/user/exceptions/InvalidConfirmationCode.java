package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidConfirmationCode extends RequestValidationException {
	public InvalidConfirmationCode(String code) {
		super(String.format("Invalid confirmation code %s", code));

	}
}
