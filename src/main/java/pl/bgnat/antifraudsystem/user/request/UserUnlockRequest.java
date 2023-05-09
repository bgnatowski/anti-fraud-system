package pl.bgnat.antifraudsystem.user.request;

public record UserUnlockRequest(
		String username,
		String operation
) {
}
