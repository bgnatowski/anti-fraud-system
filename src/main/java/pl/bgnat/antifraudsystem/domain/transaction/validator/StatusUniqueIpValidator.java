package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

import java.util.List;

@Component
class StatusUniqueIpValidator extends AbstractStatusValidator {
	protected StatusUniqueIpValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public TransactionStatus valid(TransactionRequest request, List<String> info) {
		int uniqueIp = transactionFacade.checkUniqueIpHistory(request);
		if (uniqueIp > 2) {
			setStatus(TransactionStatus.PROHIBITED);
			addIpCorrelationInfo(info);
//			info.add("ip");
		} else if (uniqueIp == 2) {
			setStatus(TransactionStatus.MANUAL_PROCESSING);
			addIpCorrelationInfo(info);
		}
		return nextValidation(request, info);
	}

	private static void addIpCorrelationInfo(List<String> info) {
		if(info.contains("none")) info.clear();
		info.add("ip-correlation");
	}
}
