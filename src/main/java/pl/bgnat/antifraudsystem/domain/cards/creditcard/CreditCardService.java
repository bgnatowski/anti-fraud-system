package pl.bgnat.antifraudsystem.domain.cards.creditcard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.account.Account;
import pl.bgnat.antifraudsystem.domain.cards.stolencard.StolenCardFacade;
import pl.bgnat.antifraudsystem.domain.exceptions.CreditCardNotFoundException;
import pl.bgnat.antifraudsystem.domain.request.CreditCardAccessRequest;
import pl.bgnat.antifraudsystem.domain.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.domain.response.CreditCardActionResponse;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final StolenCardFacade stolenCardFacade;

    CreditCard createCreditCard(User user) {
        Account account = user.getAccount();
        if (account == null)
            throw new RequestValidationException("Cannot create account without account. Create account first here: http://localhost:1102/api/auth/user/%s/account/create".formatted(user.getUsername()));
        CreditCard newCreditCard = CreditCardCreator.createNewCreditCardForCountry(account.getCountry());
        return creditCardRepository.save(newCreditCard);
    }

    @Transactional
    CreditCardActionResponse activeCreditCard(CreditCardAccessRequest creditCardAccessRequest) {
        String cardNumber = creditCardAccessRequest.cardNumber();
        CreditCard card = getCreditCardByNumber(cardNumber);

        String pin = creditCardAccessRequest.pin();

        if (!validAccess(card, pin)) {
            return createActionResponse(card, CreditCardActionResponse.getWrongPinWithAttemptsMessage(card.getValidationAttempt()));
        } else if (!validActivation(card, pin)) {
            return createActionResponse(card, CreditCardActionResponse.getWrongPinMessage());
        }

        card.setActive(true);
        card.restoreValidationAttempt();

        return createActionResponse(card, CreditCardActionResponse.getActivationMessage());
    }

    @Transactional
    CreditCardActionResponse restrict(CreditCardAccessRequest creditCardAccessRequest) {
        String cardNumber = creditCardAccessRequest.cardNumber();
        CreditCard card = getCreditCardByNumber(cardNumber);

        String pin = creditCardAccessRequest.pin();

        if (!validAccess(card, pin)) {
            return createActionResponse(card, CreditCardActionResponse.getWrongPinWithAttemptsMessage(card.getValidationAttempt()));
        }

        card.setBlocked(true);
        card.setActive(false);

        blacklist(cardNumber);
        return createActionResponse(card, CreditCardActionResponse.getRestrictionMessage(cardNumber));
    }

    CreditCardActionResponse delete(CreditCardAccessRequest creditCardAccessRequest) {
        String cardNumber = creditCardAccessRequest.cardNumber();
        CreditCard card = getCreditCardByNumber(cardNumber);

        String pin = creditCardAccessRequest.pin();

        if (!validAccess(card, pin)) {
            return createActionResponse(card, CreditCardActionResponse.getWrongPinWithAttemptsMessage(card.getValidationAttempt()));
        }

        creditCardRepository.deleteById(card.getId());
        return createActionResponse(card, CreditCardActionResponse.getDeletionMessage(cardNumber));

    }

    @Transactional
    CreditCardActionResponse changePin(CreditCardChangePinRequest creditCardChangePinRequest) {
        String cardNumber = creditCardChangePinRequest.cardNumber();
        CreditCard card = getCreditCardByNumber(cardNumber);

        String pin = creditCardChangePinRequest.oldPin();

        if (!validAccess(card, pin)) {
            return createActionResponse(card, CreditCardActionResponse.getWrongPinWithAttemptsMessage(card.getValidationAttempt()));
        }

        String newPin = creditCardChangePinRequest.newPin();
        validChangePin(card, newPin);

        card.setPin(newPin);
        card.restoreValidationAttempt();

        return CreditCardActionResponse.builder().creditCardDTO(CreditCardDTO.MAPPER.map(card)).message("Pin changed!").build();

    }

    List<CreditCardDTO> getAllCreditCards() {
        Page<CreditCard> page = creditCardRepository.findAll(Pageable.ofSize(100));
        return page.getContent().stream().map(CreditCardDTO.MAPPER).sorted(Comparator.comparingLong(CreditCardDTO::id)).collect(Collectors.toList());
    }

    CreditCardDTO getCreditCardDTOByNumber(String cardNumber) {
        return CreditCardDTO.MAPPER.map(getCreditCardByNumber(cardNumber));
    }

    void deleteCreditCardsFromAccountByUsername(String username) {
        creditCardRepository.deleteAllByAccountOwnerUsername(username);
    }

    private static CreditCardActionResponse createActionResponse(CreditCard card, String message) {
        return CreditCardActionResponse.builder()
                .creditCardDTO(CreditCardDTO.MAPPER.map(card))
                .message(message)
                .build();
    }

    private CreditCard getCreditCardByNumber(String cardNumber) {
        return creditCardRepository.findCreditCardByCardNumber(cardNumber).orElseThrow(() -> new CreditCardNotFoundException(cardNumber));
    }

    private boolean validAccess(CreditCard creditCard, String pin) {
        if (creditCard.isBlocked() || stolenCardFacade.isBlacklisted(creditCard.getCardNumber()))
            throw new RequestValidationException("Card is blocked. Contact with bank");
        if (!Objects.equals(creditCard.getPin(), pin)) {
            creditCard.increaseValidationAttempt();
            return false;
        }
        return true;
    }

    private boolean validActivation(CreditCard creditCard, String activationPin) {
        if (creditCard.isActive())
            throw new RequestValidationException("Card is already active!");
        if (Objects.equals(creditCard.getPin(), activationPin))
            return true;
        creditCard.increaseValidationAttempt();
        return false;
    }

    private void validChangePin(CreditCard card, String newPin) {
        if (!card.isActive())
            throw new RequestValidationException("Card is inactive. Contact with bank");
        if (Objects.equals(card.getPin(), newPin))
            throw new RequestValidationException("Pins are same");
    }

    private void blacklist(String cardNumber) {
        stolenCardFacade.blacklist(cardNumber);
    }
}
