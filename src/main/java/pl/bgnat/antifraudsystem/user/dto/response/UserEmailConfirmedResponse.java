package pl.bgnat.antifraudsystem.user.dto.response;

import lombok.Builder;

@Builder
public record UserEmailConfirmedResponse(String message) {
}
