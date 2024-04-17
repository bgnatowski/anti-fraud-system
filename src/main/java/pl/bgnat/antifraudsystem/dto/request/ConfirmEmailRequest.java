package pl.bgnat.antifraudsystem.dto.request;

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
        @Size(min = 4, max = 4)
        String code) {
}
