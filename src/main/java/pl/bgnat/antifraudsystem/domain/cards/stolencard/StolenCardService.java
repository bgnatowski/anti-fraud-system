package pl.bgnat.antifraudsystem.domain.cards.stolencard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.cards.CardNumberValidator;
import pl.bgnat.antifraudsystem.domain.exceptions.CardNumberFormatException;
import pl.bgnat.antifraudsystem.domain.exceptions.DuplicatedStolenCardException;
import pl.bgnat.antifraudsystem.domain.exceptions.StolenCardNotFound;
import pl.bgnat.antifraudsystem.domain.request.StolenCardRequest;
import pl.bgnat.antifraudsystem.domain.response.StolenCardDeleteResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class StolenCardService {
    private final StolenCardRepository stolenCardRepository;

    StolenCard addStolenCard(StolenCardRequest stolenCardRequest) {
        String number = stolenCardRequest.number();

        checkCardNumberFormat(number);

        if (isInDatabase(number))
            throw new DuplicatedStolenCardException(number);
        StolenCard stolenCard = StolenCard.builder().number(number).build();
        return stolenCardRepository.save(stolenCard);
    }

    StolenCardDeleteResponse deleteStolenCardByNumber(String number) {
        checkCardNumberFormat(number);
        if (!isInDatabase(number))
            throw new StolenCardNotFound(number);
        stolenCardRepository.deleteByNumber(number);
        return new StolenCardDeleteResponse(String.format("Card %s successfully removed!", number));
    }

    List<StolenCard> getAllStolenCards() {
        return stolenCardRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(StolenCard::getId))
                .collect(Collectors.toList());
    }

    boolean isBlacklisted(String number) {
        return isInDatabase(number);
    }

    private void checkCardNumberFormat(String number) {
        if (!CardNumberValidator.isValid(number))
            throw new CardNumberFormatException(number);
    }

    private boolean isInDatabase(String number) {
        return stolenCardRepository.existsByNumber(number);
    }
}
