package pl.bgnat.antifraudsystem.dto.request;

import lombok.Builder;

@Builder
public record CreditCardRestrictRequest(String cardNumber, String pin) {
}
