package pl.bgnat.antifraudsystem.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StolenCardRequest(
        @NotNull
        @NotBlank
        @Size(min = 4, max = 4, message = "Invalid pin length. Should be 4 digit")
        String number) {
}
