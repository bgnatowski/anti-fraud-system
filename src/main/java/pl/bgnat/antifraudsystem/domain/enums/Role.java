package pl.bgnat.antifraudsystem.domain.enums;

import pl.bgnat.antifraudsystem.domain.exceptions.IllegalRoleException;

public enum Role {
	ADMINISTRATOR, MERCHANT, SUPPORT;

	public static final Role parse(String role){
		try {
			return Role.valueOf(role);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new IllegalRoleException(role);
		}
	}
}
