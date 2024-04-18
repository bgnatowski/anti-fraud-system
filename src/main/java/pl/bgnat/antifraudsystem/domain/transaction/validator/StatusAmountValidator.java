package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

import java.util.List;

@Component
class StatusAmountValidator extends AbstractStatusValidator {

	protected StatusAmountValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public TransactionStatus valid(TransactionRequest request, List<String> info) {
		boolean isManualProcessing = request.amount() <= TransactionAmountValidator.MAX_AMOUNT_FOR_MANUAL_PROCESSING;

		if (!info.contains("none") )
			setStatus(isManualProcessing ? TransactionStatus.MANUAL_PROCESSING : TransactionStatus.PROHIBITED);

		return nextValidation(request, info);
	}


}
