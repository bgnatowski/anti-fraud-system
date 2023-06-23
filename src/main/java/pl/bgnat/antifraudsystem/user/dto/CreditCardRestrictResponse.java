package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record CreditCardRestrictResponse(String message) {
}
