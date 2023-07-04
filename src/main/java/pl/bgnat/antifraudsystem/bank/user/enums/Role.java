package pl.bgnat.antifraudsystem.bank.user.enums;

import pl.bgnat.antifraudsystem.bank.user.exceptions.IllegalRoleException;

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
