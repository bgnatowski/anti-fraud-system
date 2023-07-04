package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;

@Builder
public record CreditCardRestrictRequest(String cardNumber, String pin) {
}
