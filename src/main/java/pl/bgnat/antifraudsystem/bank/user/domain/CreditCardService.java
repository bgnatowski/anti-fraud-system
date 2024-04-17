package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.user.dto.*;
import pl.bgnat.antifraudsystem.bank.user.dto.request.CreditCardActivationRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.CreditCardActivationResponse;
import pl.bgnat.antifraudsystem.bank.user.dto.response.CreditCardChangePinResponse;
import pl.bgnat.antifraudsystem.bank.user.enums.Country;
import pl.bgnat.antifraudsystem.bank.user.exceptions.CreditCardNotFoundException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

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
	private final CreditCardValidator creditCardValidator;

	CreditCard createCreditCard(Country country) {
		CreditCard newCreditCard = CreditCardCreator.createNewCreditCard(country);
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
		if (!isValidCreditCardRestrictRequest(creditCardRestrictRequest))
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

	List<CreditCardDTO> getAllCreditCards() {
		Page<CreditCard> page = creditCardRepository.findAll(Pageable.ofSize(100));
		return page.getContent()
				.stream()
				.map(creditCardDTOMapper)
				.sorted(Comparator.comparingLong(CreditCardDTO::id))
				.collect(Collectors.toList());
	}

	CreditCardDTO mapToDto(CreditCard creditCard) {
		return creditCardDTOMapper.apply(creditCard);
	}

	CreditCardDTO getCreditCardDTOByNumber(String cardNumber) {
		return mapToDto(getCreditCardByNumber(cardNumber));
	}

	void deleteCreditCardsFromAccountByUsername(String username) {
		creditCardRepository.deleteAllByAccountOwnerUsername(username);
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
