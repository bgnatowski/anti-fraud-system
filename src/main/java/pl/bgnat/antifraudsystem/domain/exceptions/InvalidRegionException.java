package pl.bgnat.antifraudsystem.domain.exceptions;


import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class InvalidRegionException extends RequestValidationException {
	public InvalidRegionException(String region) {
		super(String.format("Invalid region: %s.", region));
	}
}
