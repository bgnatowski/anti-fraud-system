package pl.bgnat.antifraudsystem.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreditCardChangePinRequest(@NotNull
                                         @NotBlank
                                         @Size(min = 16, max = 16, message = "Invalid card number length")
                                         String cardNumber,
                                         @NotNull
                                         @NotBlank
                                         @Size(min = 4, max = 4, message = "Invalid old pin length. Should be 4 digit")
                                         String oldPin,
                                         @NotNull
                                         @NotBlank
                                         @Size(min = 4, max = 4, message = "Invalid new pin length. Should be 4 digit")
                                         String newPin) {
}
