package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.exceptions.CardNumberFormatException;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;

import java.util.List;

@Component
class TransactionCardNumberValidator extends AbstractTransactionValidator {

	TransactionCardNumberValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		String cardNumber = request.number();
		if(!transactionFacade.isValidCarNumber(cardNumber))
			throw new CardNumberFormatException(cardNumber);
		if (transactionFacade.isBlacklistedCardNumber(cardNumber))
			info.add("card-number");
		return nextValidation(request, info);
	}

}