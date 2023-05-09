package pl.bgnat.antifraudsystem.user;

public record UserUpdateRoleRequest(
		String username,
		Role role
) {
}
