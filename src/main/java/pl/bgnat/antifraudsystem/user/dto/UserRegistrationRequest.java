package pl.bgnat.antifraudsystem.user.dto;

public record UserRegistrationRequest(String name,
									  String username,
									  String password) {
}
