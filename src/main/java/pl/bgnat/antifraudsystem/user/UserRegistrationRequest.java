package pl.bgnat.antifraudsystem.user;

public record UserRegistrationRequest(String name,
									  String username,
									  String password) {
}
