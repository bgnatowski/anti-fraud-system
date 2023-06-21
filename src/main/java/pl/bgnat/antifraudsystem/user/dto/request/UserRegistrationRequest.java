package pl.bgnat.antifraudsystem.user.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserRegistrationRequest(String firstName,
									  String lastName,
									  String email,
									  String username,
									  String password,
									  String phoneNumber,
									  LocalDate dateOfBirth) {
}
