package pl.bgnat.antifraudsystem.bank.user.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class AdministratorRoleCannotBeChangedException extends RequestValidationException {
	public static final String CANNOT_BLOCK_ADMIN_MESSAGE = "Cannot block administrator! The role should be SUPPORT or MERCHANT";
	public AdministratorRoleCannotBeChangedException() {
		super(CANNOT_BLOCK_ADMIN_MESSAGE);
	}
}
