package pl.bgnat.antifraudsystem.transaction.stolenCards;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.transaction.stolenCards.dto.StolenCardDeleteResponse;
import pl.bgnat.antifraudsystem.transaction.stolenCards.dto.StolenCardRequest;
import pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.CardNumberFormatException;
import pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.DuplicatedStolenCardException;
import pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.StolenCardNotFound;
import pl.bgnat.antifraudsystem.utils.CardNumberValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
@Service
class StolenCardService {
	private final StolenCardRepository stolenCardRepository;

	StolenCardService(StolenCardRepository stolenCardRepository,
					  CardNumberValidator cardNumberValidator) {
		this.stolenCardRepository = stolenCardRepository;
	}


	StolenCard addStolenCard(StolenCardRequest stolenCardRequest) {
		String number = stolenCardRequest.number();
		if(!isValidJsonFormat(number))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		checkCardNumberFormat(number);

		if(isAlreadyInDb(number))
			throw new DuplicatedStolenCardException(number);
		StolenCard stolenCard = StolenCard.builder().number(number).build();
		return stolenCardRepository.save(stolenCard);
	}

	StolenCardDeleteResponse deleteStolenCardByNumber(String number) {
		checkCardNumberFormat(number);
		if(!isAlreadyInDb(number))
			throw new StolenCardNotFound(number);
		stolenCardRepository.deleteByNumber(number);
		return new StolenCardDeleteResponse(String.format("Card %s successfully removed!", number));
	}

	private void checkCardNumberFormat(String number) {
		if(!isValidCardNumber(number))
			throw new CardNumberFormatException(number);
	}

	public boolean isBlacklisted(String number) {
		return isAlreadyInDb(number);
	}

	List<StolenCard> getAllStolenCards() {
		return stolenCardRepository.findAll()
				.stream()
				.sorted(Comparator.comparingLong(StolenCard::getId))
				.collect(Collectors.toList());
	}


	boolean isValidCardNumber(String number) {
		return CardNumberValidator.isValid(number);
	}

	private boolean isValidJsonFormat(String number) {
		return number != null;
	}

	private boolean isAlreadyInDb(String number) {
		return stolenCardRepository.existsByNumber(number);
	}
}
