package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;

@Builder
public record UserUnlockResponse(
		String status
) {
	public static final String MESSAGE_PATTERN = "User %s %s";
}
