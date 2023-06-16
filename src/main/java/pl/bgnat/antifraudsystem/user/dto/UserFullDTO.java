package pl.bgnat.antifraudsystem.user.dto;


import pl.bgnat.antifraudsystem.user.Role;

public record UserFullDTO(Long id,
						  String name,
						  String username,
						  Role role,
						  String address,
						  String phoneNumber) {
}
