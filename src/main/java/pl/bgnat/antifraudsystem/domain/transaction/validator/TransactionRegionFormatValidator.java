package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.exceptions.InvalidRegionException;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.dto.TransactionRegion;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;

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
