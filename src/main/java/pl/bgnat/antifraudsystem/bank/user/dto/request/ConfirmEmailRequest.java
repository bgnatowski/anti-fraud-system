package pl.bgnat.antifraudsystem.bank.user.dto.request;

import lombok.Builder;

@Builder
public record ConfirmEmailRequest(String username, String code) {
}
