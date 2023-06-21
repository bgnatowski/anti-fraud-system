package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IllegalCountryException extends RequestValidationException {
	public static final String INVALID_COUNTRY_S_PATTERN = "Invalid country: %s";
	public IllegalCountryException(String country) {
		super(String.format(INVALID_COUNTRY_S_PATTERN, country));
	}
}
