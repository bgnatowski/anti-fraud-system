package pl.bgnat.antifraudsystem.domain.cards.stolencard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.cards.CardNumberValidator;
import pl.bgnat.antifraudsystem.domain.request.StolenCardRequest;
import pl.bgnat.antifraudsystem.domain.response.StolenCardDeleteResponse;

@Component
@RequiredArgsConstructor
public class StolenCardFacade {
    private final StolenCardService stolenCardService;

    public boolean isValid(String cardNumber) {
        return CardNumberValidator.isValid(cardNumber);
    }

    public boolean isBlacklisted(String number) {
        return this.stolenCardService.isBlacklisted(number);
    }

    public String blacklist(String number) {
        StolenCardRequest request = new StolenCardRequest(number);
        StolenCard stolenCard = stolenCardService.addStolenCard(request);
        return stolenCard.toString();
    }

    public String delete(String number) {
        StolenCardDeleteResponse stolenCardDeleteResponse = stolenCardService.deleteStolenCardByNumber(number);
        return stolenCardDeleteResponse.status();
    }

}
