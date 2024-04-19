package pl.bgnat.antifraudsystem.domain.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record AddressRegisterRequest(
        @NonNull
        @NotBlank
        String addressLine1,
        String addressLine2,
        @NonNull
        @NotBlank
        String postalCode,
        @NonNull
        @NotBlank
        String city,
        @NonNull
        @NotBlank
        String state,
        @NonNull
        @NotBlank
        @Pattern(regexp = "^(China|India|Japan|Brazil|Poland|United States|Germany|France|United Kingdom|Australia|Russia|South Korea)$",
                message = "We currently support clients from: China|India|Japan|Brazil|Poland|United States|Germany|France|United Kingdom|Australia|Russia|South Korea")
        String country
) {
}
