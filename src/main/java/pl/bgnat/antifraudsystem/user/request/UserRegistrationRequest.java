package pl.bgnat.antifraudsystem.user.request;

public record UserRegistrationRequest(String name,
									  String username,
									  String password) {
}
