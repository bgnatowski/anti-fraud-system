package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IllegalChangeLockOperationException extends RequestValidationException {
	public static final String INVALID_OPERATION_S_PATTERN = "Invalid operation: %s";

	public IllegalChangeLockOperationException(String operation) {
		super(String.format(INVALID_OPERATION_S_PATTERN, operation));
	}
}
