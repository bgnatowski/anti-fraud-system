package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.bank.user.dto.AccountDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;

@Builder
public record UserAccountCreatedResponse(
        UserDTO userDTO,
        AccountDTO accountDTO
) {
}
