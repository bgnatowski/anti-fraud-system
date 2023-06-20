package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record AddressDTO(
		String addressLine1,
		String addressLine2,
		String postalCode,
		String city,
		String state,
		String country
		) {
	public static AddressDTO emptyAddress() {
		return new AddressDTO("", "", "", "", "", "");
	}
}
