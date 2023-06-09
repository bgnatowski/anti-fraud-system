package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;

@Builder
public record PhoneNumberDTO(String areaCode, String number) {
	public static PhoneNumberDTO emptyDto() {
		return new PhoneNumberDTO("", "");
	}
}
