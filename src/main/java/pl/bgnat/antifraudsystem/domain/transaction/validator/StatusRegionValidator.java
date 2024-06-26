package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

import java.util.List;

@Component
class StatusRegionValidator extends AbstractStatusValidator {

	protected StatusRegionValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public TransactionStatus valid(TransactionRequest request, List<String> info) {
		int regionHistory = transactionFacade.checkRegionHistory(request);
		if (regionHistory > 2) {
			setStatus(TransactionStatus.PROHIBITED);
			addRegionCorrelationInfo(info);
//			transactionFacade.blacklistCardNumber(request.number());
//			info.add("card-number");
		} else if (regionHistory == 2) {
			setStatus(TransactionStatus.MANUAL_PROCESSING);
			addRegionCorrelationInfo(info);
		}
		return nextValidation(request, info);
	}

	private static void addRegionCorrelationInfo(List<String> info) {
		if(info.contains("none")) info.clear();
		info.add("region-correlation");
	}


}
