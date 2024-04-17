package pl.bgnat.antifraudsystem.domain.creditcard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.dto.CreditCardDTO;

@Component
@RequiredArgsConstructor
public class CreditCardFacade {
    private final CreditCardService creditCardService;

    public CreditCard createCreditCard(Country country) {
        return creditCardService.createCreditCard(country);
    }

    public CreditCardDTO mapToDto(CreditCard newCreditCard) {
        return creditCardService.mapToDto(newCreditCard);
    }

    public void deleteCreditCardsFromAccountByUsername(String username) {
        creditCardService.deleteCreditCardsFromAccountByUsername(username);
    }
}
