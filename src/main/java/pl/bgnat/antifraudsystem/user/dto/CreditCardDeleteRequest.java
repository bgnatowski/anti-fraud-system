package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record CreditCardDeleteRequest(String cardNumber, String pin) {
}
