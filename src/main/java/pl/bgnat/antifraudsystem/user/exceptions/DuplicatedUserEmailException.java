package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUserEmailException extends DuplicateResourceException {
	public static final String USER_WITH_EMAIL_S_ALREADY_EXISTS = "User with EMAIL = %s already exists";
	public DuplicatedUserEmailException(String email) {
		super(String.format(USER_WITH_EMAIL_S_ALREADY_EXISTS, email));
	}
}
