package pl.bgnat.antifraudsystem.bank.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.validation.exceptions.InvalidRegionException;
import pl.bgnat.antifraudsystem.bank.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRegion;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;

import java.util.List;

@Component
class TransactionRegionFormatValidator extends AbstractTransactionValidator{

	TransactionRegionFormatValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		String regionString = request.region();
		try{
			TransactionRegion.valueOf(regionString);
		}catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidRegionException(regionString);
		}
		return nextValidation(request, info);
	}

}
