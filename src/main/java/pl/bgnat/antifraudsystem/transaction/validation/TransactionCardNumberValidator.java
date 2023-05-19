package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.CardNumberFormatException;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.stolenCards.StolenCardFacade;

import java.util.List;

@Component
class TransactionCardNumberValidator extends AbstractValidator {
	private final StolenCardFacade stolenCardFacade;
	TransactionCardNumberValidator(StolenCardFacade facade) {
		this.stolenCardFacade = facade;
	}

	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		String cardNumber = request.number();
		if(!stolenCardFacade.isValid(cardNumber))
			throw new CardNumberFormatException(cardNumber);
		if (stolenCardFacade.isBlacklisted(cardNumber))
			info.add("card-number");
		return nextValidation(request, info);
	}

}