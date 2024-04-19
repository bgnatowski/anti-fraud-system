package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedUserRoleException extends DuplicateResourceException {
	public static final String ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S_PATTERN = "Role: %s is already assigned to the user with username = %s";
	public DuplicatedUserRoleException(String role, String username) {
		super(String.format(ROLE_S_IS_ALREADY_ASSIGNED_TO_THE_USER_WITH_USERNAME_S_PATTERN, role, username));
	}
}
