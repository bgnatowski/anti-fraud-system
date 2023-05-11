package pl.bgnat.antifraudsystem.user.request;

import org.springframework.lang.NonNull;

public record UserUnlockRequest(
		String username,
		String operation
) {
}
