package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record PhoneNumberDTO(String number) {
	public static PhoneNumberDTO emptyPhone() {
		return new PhoneNumberDTO("");
	}
}
