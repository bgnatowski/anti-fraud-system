package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;

import java.util.List;

@Component
class TransactionIpValidator extends AbstractTransactionValidator {
	TransactionIpValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		if (transactionFacade.isBlacklistedIp(request.ip()))
			info.add("ip");
		return nextValidation(request, info);
	}
}
