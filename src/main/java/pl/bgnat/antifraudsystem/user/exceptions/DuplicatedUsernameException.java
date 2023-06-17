package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUsernameException extends DuplicateResourceException {
	public static final String USER_WITH_USERNAME_S_ALREADY_EXISTS = "User with username = %s already exists";
	public DuplicatedUsernameException(String username) {
		super(String.format(USER_WITH_USERNAME_S_ALREADY_EXISTS, username));
	}
}
