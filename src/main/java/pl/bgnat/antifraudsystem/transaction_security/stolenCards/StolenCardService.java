package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.stolenCard.DuplicatedStolenCardException;
import pl.bgnat.antifraudsystem.exception.stolenCard.StolenCardNotFound;
import pl.bgnat.antifraudsystem.transaction_security.validation.SecurityValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
@Service
class StolenCardService {
	private final StolenCardRepository stolenCardRepository;
	private final SecurityValidator<String> cardNumberValidator;

	StolenCardService(StolenCardRepository stolenCardRepository,
					  @Qualifier("CardNumberValidator") SecurityValidator<String> cardNumberValidator) {
		this.stolenCardRepository = stolenCardRepository;
		this.cardNumberValidator = cardNumberValidator;
	}


	StolenCard addStolenCard(StolenCardRequest stolenCardRequest) {
		String number = stolenCardRequest.number();
		if(!isValidJsonFormat(number))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		if(!isValidCardNumber(number))
			throw new CardNumberFormatException(number);
		if(isAlreadyInDb(number))
			throw new DuplicatedStolenCardException(number);
		StolenCard stolenCard = StolenCard.builder().number(number).build();
		return stolenCardRepository.save(stolenCard);
	}

	StolenCardDeleteResponse deleteStolenCardByNumber(String number) {
		if(!isAlreadyInDb(number))
			throw new StolenCardNotFound(number);
		stolenCardRepository.deleteByNumber(number);
		return new StolenCardDeleteResponse(String.format("Card %s successfully removed!", number));
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
		return cardNumberValidator.isValid(number);
	}

	private boolean isValidJsonFormat(String number) {
		return number != null;
	}

	private boolean isAlreadyInDb(String number) {
		return stolenCardRepository.existsByNumber(number);
	}
}
