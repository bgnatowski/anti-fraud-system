package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;

@Builder
public record UserEmailConfirmedResponse(String message) {
	public static final String EMAIL_CONFIRMED_MESSAGE = "User email confirmed";
}
