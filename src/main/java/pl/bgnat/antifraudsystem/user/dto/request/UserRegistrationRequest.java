package pl.bgnat.antifraudsystem.user.dto.request;

import lombok.Builder;

@Builder
public record UserRegistrationRequest(String firstName,
									  String lastName,
									  String email,
									  String username,
									  String password,
									  String phoneNumber) {
}
