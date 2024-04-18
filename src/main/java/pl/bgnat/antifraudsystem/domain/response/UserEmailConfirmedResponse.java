package pl.bgnat.antifraudsystem.domain.response;

import lombok.Builder;

@Builder
public record UserEmailConfirmedResponse(String message) {
	public static final String EMAIL_CONFIRMED_MESSAGE = "User email confirmed";
	public static final String EMAIL_ALREADY_CONFIRMED_MESSAGE = "User is already confirmed";
}
