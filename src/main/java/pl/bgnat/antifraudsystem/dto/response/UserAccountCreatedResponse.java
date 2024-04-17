package pl.bgnat.antifraudsystem.dto.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.dto.AccountDTO;
import pl.bgnat.antifraudsystem.dto.UserDTO;

@Builder
public record UserAccountCreatedResponse(
        UserDTO userDTO,
        AccountDTO accountDTO
) {
}
