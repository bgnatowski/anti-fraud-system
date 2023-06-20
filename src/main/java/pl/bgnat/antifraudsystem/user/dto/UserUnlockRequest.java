package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record UserUnlockRequest(
		String username,
		String operation
) {
}
