package pl.bgnat.antifraudsystem.dto.request;

import lombok.Builder;

@Builder
public record CreditCardDeleteRequest(String cardNumber, String pin) {
}
