package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class AdministratorCannotBeLockException extends RequestValidationException {
	public static final String CANNOT_BLOCK_ADMINISTRATOR = "Cannot block administrator!";
	public AdministratorCannotBeLockException() {
		super(CANNOT_BLOCK_ADMINISTRATOR);
	}
}
