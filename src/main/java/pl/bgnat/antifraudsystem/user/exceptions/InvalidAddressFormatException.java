package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidAddressFormatException extends RequestValidationException {

	public static final String INVALID_ADDRESS_FORMAT_S = "Invalid address format: %s";

	public InvalidAddressFormatException(String address) {
		super(String.format(INVALID_ADDRESS_FORMAT_S, address));
	}
}
