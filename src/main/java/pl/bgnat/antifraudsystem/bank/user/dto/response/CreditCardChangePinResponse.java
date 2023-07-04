package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;

@Builder
public record CreditCardChangePinResponse(String message) {
}
