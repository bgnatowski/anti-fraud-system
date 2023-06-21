package pl.bgnat.antifraudsystem.user.dto;

import lombok.Builder;
import pl.bgnat.antifraudsystem.user.enums.Role;

@Builder
public record UserDTO(Long id,
					  String firstName,
					  String lastName,
					  String username,
					  String email,
					  Role role,
					  boolean isActive,
					  AddressDTO address,
					  PhoneNumberDTO phoneNumber,
					  TemporaryAuthorizationDTO temporaryAuthorization) {
}
