package pl.bgnat.antifraudsystem.user.dto;

import pl.bgnat.antifraudsystem.user.Role;

public record UserDTO(Long id,
					  String name,
					  String username,
					  Role role) {
}
