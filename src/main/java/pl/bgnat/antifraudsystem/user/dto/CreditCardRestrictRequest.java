package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record CreditCardRestrictRequest(String cardNumber, String pin) {
}
