
package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.utils.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CreditCardDTOMapper implements Mapper<CreditCard, CreditCardDTO> {
    private static Mapper<CreditCard, CreditCardDTO> instance;

    static Mapper<CreditCard, CreditCardDTO> getMapper() {
        if (instance == null) {
            instance = new CreditCardDTOMapper();
        }
        return instance;
    }

    @Override
    public CreditCardDTO apply(CreditCard creditCard) {
        if (creditCard == null) return null;
        CreditCardDTO.CreditCardDTOBuilder builder = CreditCardDTO.builder()
                .id(creditCard.getId())
                .cardNumber(creditCard.getCardNumber())
                .active(creditCard.isActive());
        Account account = creditCard.getAccount();
        if (account != null){
            builder.accountId(account.getId());
            builder.ownerId(account.getOwner().getId());
        }

        return builder.build();
    }

    @Override
    public CreditCardDTO map(CreditCard model) {
        return apply(model);
    }
}
