package pl.bgnat.antifraudsystem.user;

public enum Role {
	ADMINISTRATOR("ROLE_ADMINISTRATOR"), MERCHANT("ROLE_MERCHANT"), SUPPORT("ROLE_SUPPORT");

	private final String fullRoleName;

	Role(String fullRoleName) {
		this.fullRoleName = fullRoleName;
	}

	public String getFullRoleName() {
		return fullRoleName;
	}
}
