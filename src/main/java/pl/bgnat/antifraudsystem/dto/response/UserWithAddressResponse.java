package pl.bgnat.antifraudsystem.dto.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.dto.AddressDTO;
import pl.bgnat.antifraudsystem.dto.UserDTO;

@Builder
public record UserWithAddressResponse(
        UserDTO userDTO,
        AddressDTO addressDTO
) {
}
