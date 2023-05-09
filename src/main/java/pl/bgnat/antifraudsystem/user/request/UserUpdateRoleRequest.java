package pl.bgnat.antifraudsystem.user.request;

public record UserUpdateRoleRequest(
		String username,
		String role
) {
}
