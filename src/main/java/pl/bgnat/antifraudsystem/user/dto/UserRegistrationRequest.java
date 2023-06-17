package pl.bgnat.antifraudsystem.user.dto;

public record UserRegistrationRequest(String firstName,
									  String lastName,
									  String email,
									  String username,
									  String password) {
}
