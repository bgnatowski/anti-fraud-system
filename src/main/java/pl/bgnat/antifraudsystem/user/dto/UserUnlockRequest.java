package pl.bgnat.antifraudsystem.user.dto;

public record UserUnlockRequest(
		String username,
		String operation
) {
}
