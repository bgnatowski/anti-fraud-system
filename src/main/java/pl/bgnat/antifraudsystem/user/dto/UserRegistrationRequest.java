package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;

@Builder
public record UserRegistrationRequest(String firstName,
									  String lastName,
									  String email,
									  String username,
									  String password) {
}
