package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IllegalRoleException extends RequestValidationException {
	static final String INVALID_ROLE_S_PATTERN = "Invalid role: %s";
	public IllegalRoleException(String role) {
		super(String.format(INVALID_ROLE_S_PATTERN, role));

	}
}
