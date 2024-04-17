package pl.bgnat.antifraudsystem.dto.request;

import lombok.Builder;

@Builder
public record UserUnlockRequest(
		String username,
		String operation
) {
	public static final String UNLOCK = "UNLOCK";
	public static final String LOCK = "LOCK";
}
