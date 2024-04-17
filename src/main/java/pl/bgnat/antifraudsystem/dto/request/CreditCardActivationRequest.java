package pl.bgnat.antifraudsystem.dto.request;

import lombok.Builder;

@Builder
public record CreditCardActivationRequest(String cardNumber, String activationPin) {
}
