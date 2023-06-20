package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateRoleRequest(String username,
							 String role) {
}
