package pl.bgnat.antifraudsystem.user;

public record UserDTO(Long id,
					  String name,
					  String username,
					  Role role) {
}
