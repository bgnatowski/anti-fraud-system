package pl.bgnat.antifraudsystem.bank.user.dto.request;

import lombok.Builder;

@Builder
public record CreditCardChangePinRequest(String cardNumber, String oldPin, String newPin) {
}
