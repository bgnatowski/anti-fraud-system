package pl.bgnat.antifraudsystem.user.dto;


import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record AddressRegisterRequest(
		@NonNull String addressLine1,
		@NonNull String addressLine2,
		@NonNull String postalCode,
		@NonNull String city,
		@NonNull String state,
		@NonNull String country
) {
}
