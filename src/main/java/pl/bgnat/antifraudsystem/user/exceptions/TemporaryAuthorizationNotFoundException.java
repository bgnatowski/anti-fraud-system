package pl.bgnat.antifraudsystem.user.exceptions;

import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;

public class TemporaryAuthorizationNotFoundException extends ResourceNotFoundException {
	public static final String THERE_IS_NO_TEMP_AUTH_FOR_USER_S = "There is no temp auth for user = %s";
	public TemporaryAuthorizationNotFoundException(String username) {
		super(String.format(THERE_IS_NO_TEMP_AUTH_FOR_USER_S, username));
	}
}
