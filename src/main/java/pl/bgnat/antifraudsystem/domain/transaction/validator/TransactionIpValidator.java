package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.exceptions.IpFormatException;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;

import java.util.List;

@Component
class TransactionIpValidator extends AbstractTransactionValidator {
	TransactionIpValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		String ip = request.ip();
		if(!transactionFacade.isValidIp(ip))
			throw new IpFormatException(ip);
		if (transactionFacade.isBlacklistedIp(request.ip()))
			info.add("ip");
		return nextValidation(request, info);
	}
}
