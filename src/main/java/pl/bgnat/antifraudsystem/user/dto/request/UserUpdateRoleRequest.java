package pl.bgnat.antifraudsystem.user.dto.request;

import lombok.Builder;

@Builder
public record UserUpdateRoleRequest(String username,
							 String role) {
}
