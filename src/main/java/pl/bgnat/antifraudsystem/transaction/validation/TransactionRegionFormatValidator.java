package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.transaction.dto.Region;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.validation.exceptions.InvalidRegionException;

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
			Region.valueOf(regionString);
		}catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidRegionException(regionString);
		}
		return nextValidation(request, info);
	}

}
