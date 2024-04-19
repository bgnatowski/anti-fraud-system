package pl.bgnat.antifraudsystem.domain.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.account.AccountDTO;
import pl.bgnat.antifraudsystem.domain.user.UserDTO;

@Builder
public record UserAccountCreatedResponse(
        UserDTO userDTO,
        AccountDTO accountDTO
) {
}
