package pl.bgnat.antifraudsystem.dto.response;

import lombok.Builder;

@Builder
public record CreditCardDeleteResponse(String message) {
}