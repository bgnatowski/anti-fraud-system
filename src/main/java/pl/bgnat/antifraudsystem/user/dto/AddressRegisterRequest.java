package pl.bgnat.antifraudsystem.user.dto;


import org.springframework.lang.NonNull;

public record AddressRegisterRequest(
		@NonNull String addressLine1,
		String addressLine2,
		@NonNull String postalCode,
		@NonNull String city,
		@NonNull String state,
		@NonNull String country
) {
}
