package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.stolenCard.DuplicatedStolenCardException;
import pl.bgnat.antifraudsystem.exception.stolenCard.StolenCardNotFound;
import pl.bgnat.antifraudsystem.transaction.TransactionValidator;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.request.StolenCardRequest;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.response.StolenCardDeleteResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
@Service
class StolenCardService {
	private final StolenCardRepository stolenCardRepository;
	private final TransactionValidator transactionValidator;

	StolenCardService(StolenCardRepository stolenCardRepository, TransactionValidator transactionValidator) {
		this.stolenCardRepository = stolenCardRepository;
		this.transactionValidator = transactionValidator;
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


	private boolean isValidJsonFormat(String number) {
		return number != null;
	}

	private boolean isValidCardNumber(String number) {
		return transactionValidator.isValidCardNumber(number);
	}

	private boolean isAlreadyInDb(String number) {
		return stolenCardRepository.existsByNumber(number);
	}
}
