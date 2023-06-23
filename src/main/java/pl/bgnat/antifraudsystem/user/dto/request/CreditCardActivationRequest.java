package pl.bgnat.antifraudsystem.user.dto.request;

import lombok.Builder;

@Builder
public record CreditCardActivationRequest(String cardNumber, String activationPin) {
}
