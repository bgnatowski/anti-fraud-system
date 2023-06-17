package pl.bgnat.antifraudsystem.user.dto;

public record PhoneNumberDTO(String number) {
	public static PhoneNumberDTO emptyPhone() {
		return new PhoneNumberDTO("");
	}
}
