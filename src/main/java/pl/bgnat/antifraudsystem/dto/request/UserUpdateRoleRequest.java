package pl.bgnat.antifraudsystem.dto.request;

import lombok.Builder;

@Builder
public record UserUpdateRoleRequest(String username,
							 String role) {
}
