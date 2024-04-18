package pl.bgnat.antifraudsystem.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ConfirmEmailRequest(
        @NotNull
        @NotBlank
        String username,
        @NotNull
        @NotBlank
        @Size(min = 5, max = 5, message = "Code should be 5 digit length")
        String code) {
}