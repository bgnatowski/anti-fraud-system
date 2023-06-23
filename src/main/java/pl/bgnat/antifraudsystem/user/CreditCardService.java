package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.dto.request.CreditCardActivationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.user.dto.response.CreditCardActivationResponse;
import pl.bgnat.antifraudsystem.user.dto.response.CreditCardChangePinResponse;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.exceptions.CreditCardNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
@RequiredArgsConstructor
class CreditCardService {
	private final CreditCardRepository creditCardRepository;
	private final CreditCardDTOMapper creditCardDTOMapper;
	private final CreditCardCreator creditCardCreator;
	private final CreditCardValidator creditCardValidator;

	CreditCard createCreditCard(Country country) {
		CreditCard newCreditCard = creditCardCreator.createNewCreditCard(country);
		return creditCardRepository.save(newCreditCard);
	}

	CreditCardActivationResponse activeCreditCard(CreditCardActivationRequest creditCardActiveRequest) {
		if (!isValidActivationCreditCardRequest(creditCardActiveRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String cardNumber = creditCardActiveRequest.cardNumber();
		String activationPin = creditCardActiveRequest.activationPin();

		CreditCard card = getCreditCardByNumber(cardNumber);
		creditCardValidator.accessValidation(card, activationPin);
		creditCardValidator.activationValidation(card, activationPin);

		card.setActive(true);
		card.restoreValidationAttempt();
		creditCardRepository.save(card);

		return CreditCardActivationResponse.builder()
				.message(CreditCardActivationResponse.CREDIT_CARD_ACTIVATED)
				.build();
	}

	CreditCardChangePinResponse changePin(CreditCardChangePinRequest creditCardChangePinRequest) {
		if (!isValidChangePinRequest(creditCardChangePinRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String newPin = creditCardChangePinRequest.newPin();
		String oldPin = creditCardChangePinRequest.oldPin();
		String cardNumber = creditCardChangePinRequest.cardNumber();

		CreditCard card = getCreditCardByNumber(cardNumber);
		creditCardValidator.accessValidation(card, oldPin);
		creditCardValidator.changingPinValidation(card, newPin);

		card.setPin(newPin);
		card.restoreValidationAttempt();
		creditCardRepository.save(card);

		return CreditCardChangePinResponse.builder()
				.message("Pin changed!")
				.build();
	}

	void deleteCreditCardsFromAccountByUsername(String username) {
		creditCardRepository.deleteAllByAccountOwnerUsername(username);
	}

	CreditCardDeleteResponse delete(CreditCardDeleteRequest creditCardDeleteRequest) {
		if (!isValidCreditCardDeleteRequest(creditCardDeleteRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		String cardNumber = creditCardDeleteRequest.cardNumber();
		String pin = creditCardDeleteRequest.pin();
		CreditCard card = getCreditCardByNumber(cardNumber);
		creditCardValidator.accessValidation(card, pin);

		creditCardRepository.deleteById(card.getId());
		return CreditCardDeleteResponse.builder()
				.message("Credit card deleted")
				.build();
	}

	CreditCardRestrictResponse restrict(CreditCardRestrictRequest creditCardRestrictRequest) {
		if(!isValidCreditCardRestrictRequest(creditCardRestrictRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		String cardNumber = creditCardRestrictRequest.cardNumber();
		String pin = creditCardRestrictRequest.pin();
		CreditCard card = getCreditCardByNumber(cardNumber);
		creditCardValidator.accessValidation(card, pin);

		card.setBlocked(true);
		creditCardRepository.save(card);
		return CreditCardRestrictResponse.builder()
				.message("Credit card restricted")
				.build();
	}

	public List<CreditCardDTO> getAllCreditCards() {
		Page<CreditCard> page = creditCardRepository.findAll(Pageable.ofSize(100));
		return page.getContent()
				.stream()
				.map(creditCardDTOMapper)
				.sorted(Comparator.comparingLong(CreditCardDTO::id))
				.collect(Collectors.toList());
	}

	private CreditCardDTO mapToDto(CreditCard creditCard) {
		return creditCardDTOMapper.apply(creditCard);
	}

	CreditCardDTO getCreditCardDTOByNumber(String cardNumber){
		return mapToDto(getCreditCardByNumber(cardNumber));
	}

	private CreditCard getCreditCardByNumber(String cardNumber) {
		return creditCardRepository.findCreditCardByCardNumber(cardNumber)
				.orElseThrow(() -> new CreditCardNotFoundException(cardNumber));
	}

	private boolean isValidCreditCardRestrictRequest(CreditCardRestrictRequest creditCardRestrictRequest) {
		return Stream.of(creditCardRestrictRequest.cardNumber(),
						creditCardRestrictRequest.pin())
				.noneMatch(s -> s == null || s.isEmpty());
	}

	private boolean isValidCreditCardDeleteRequest(CreditCardDeleteRequest creditCardDeleteRequest) {
		return Stream.of(creditCardDeleteRequest.cardNumber(),
						creditCardDeleteRequest.pin())
				.noneMatch(s -> s == null || s.isEmpty());
	}

	private boolean isValidActivationCreditCardRequest(CreditCardActivationRequest creditCardActivationRequest) {
		return Stream.of(creditCardActivationRequest.cardNumber(),
						creditCardActivationRequest.activationPin())
				.noneMatch(s -> s == null || s.isEmpty());
	}

	private boolean isValidChangePinRequest(CreditCardChangePinRequest creditCardActiveRequest) {
		return Stream.of(creditCardActiveRequest.cardNumber(),
						creditCardActiveRequest.oldPin(),
						creditCardActiveRequest.newPin())
				.noneMatch(s -> s == null || s.isEmpty());
	}
}
