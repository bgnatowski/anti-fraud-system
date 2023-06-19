package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IllegalCountryException extends RequestValidationException {
	public IllegalCountryException(String country) {
		super(String.format("Cannot create IBAN account number with incomplete user address"));
	}
}
