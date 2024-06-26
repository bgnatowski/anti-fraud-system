package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

import java.util.List;

@Component
class PreStatusValidator extends AbstractStatusValidator{
	protected PreStatusValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public TransactionStatus valid(TransactionRequest request, List<String> info) {
		if(isContainingBlacklisted(info))
			setStatus(TransactionStatus.PROHIBITED);
		return nextValidation(request, info);
	}

	private static boolean isContainingBlacklisted(List<String> info) {
		return info.contains("ip") || info.contains("card-number");
	}
}
