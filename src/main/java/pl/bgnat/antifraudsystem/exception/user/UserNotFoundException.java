package pl.bgnat.antifraudsystem.exception.user;


import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
	public static final String THERE_IS_NO_USER_WITH_USERNAME_S = "There is no user with username = %s";
	public UserNotFoundException(String username) {
		super(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));
	}
}
