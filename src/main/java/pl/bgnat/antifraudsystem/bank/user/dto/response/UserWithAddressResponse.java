package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.bank.user.dto.AddressDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;

@Builder
public record UserWithAddressResponse(
        UserDTO userDTO,
        AddressDTO addressDTO
) {
}
