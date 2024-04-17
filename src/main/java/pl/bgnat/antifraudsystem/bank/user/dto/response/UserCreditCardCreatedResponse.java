package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.bank.user.dto.AccountDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.CreditCardDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;

@Builder
public record UserCreditCardCreatedResponse(
        UserDTO userDTO,

        AccountDTO accountDTO,
        CreditCardDTO creditCardDTO
) {
}
