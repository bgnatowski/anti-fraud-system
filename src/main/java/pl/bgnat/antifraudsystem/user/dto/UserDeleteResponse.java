package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record UserDeleteResponse(String username, String status) {
}
