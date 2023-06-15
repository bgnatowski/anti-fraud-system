package pl.bgnat.antifraudsystem.transaction.validation.exceptions;


import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidRegionException extends RequestValidationException {
	public InvalidRegionException(String region) {
		super(String.format("Invalid region: %s.", region));
	}
}
