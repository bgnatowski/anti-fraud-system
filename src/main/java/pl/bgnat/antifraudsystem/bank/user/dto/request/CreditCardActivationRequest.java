package pl.bgnat.antifraudsystem.bank.user.dto.request;

import lombok.Builder;

@Builder
public record CreditCardActivationRequest(String cardNumber, String activationPin) {
}
