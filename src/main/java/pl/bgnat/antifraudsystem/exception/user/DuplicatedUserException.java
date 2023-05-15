package pl.bgnat.antifraudsystem.exception.user;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUserException extends DuplicateResourceException {
	public static final String USER_WITH_USERNAME_S_ALREADY_EXISTS = "User with username = %s already exists";
	public DuplicatedUserException(String username) {
		super(String.format(USER_WITH_USERNAME_S_ALREADY_EXISTS, username));
	}
}
