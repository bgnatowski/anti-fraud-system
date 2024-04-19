package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.user.User;

@Component
@RequiredArgsConstructor
public class CreditCardFacade {
    private final CreditCardService creditCardService;

    public CreditCard createCreditCardForUser(User user) {
        return creditCardService.createCreditCard(user);
    }

    public void deleteCreditCardsFromAccountByUsername(String username) {
        creditCardService.deleteCreditCardsFromAccountByUsername(username);
    }
}
