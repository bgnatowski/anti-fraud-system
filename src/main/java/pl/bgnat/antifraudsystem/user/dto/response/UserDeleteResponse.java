package pl.bgnat.antifraudsystem.user.dto.response;

import lombok.Builder;

@Builder
public record UserDeleteResponse(String username, String status) {
}
