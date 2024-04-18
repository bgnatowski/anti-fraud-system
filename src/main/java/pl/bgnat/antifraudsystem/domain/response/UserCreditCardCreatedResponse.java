package pl.bgnat.antifraudsystem.domain.response;

import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.account.AccountDTO;
import pl.bgnat.antifraudsystem.domain.cards.creditcard.CreditCardDTO;
import pl.bgnat.antifraudsystem.domain.user.UserDTO;

@Builder
public record UserCreditCardCreatedResponse(
        UserDTO userDTO,

        AccountDTO accountDTO,
        CreditCardDTO creditCardDTO
) {
}
