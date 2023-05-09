package pl.bgnat.antifraudsystem.user;

public record UserUnlockRequest(
		String username,
		String operation
) {
}
