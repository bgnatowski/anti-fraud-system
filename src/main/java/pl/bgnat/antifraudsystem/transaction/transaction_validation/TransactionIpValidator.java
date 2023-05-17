package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.SuspiciousIPFacade;

import java.util.List;

@Component
class TransactionIpValidator extends AbstractValidator {
	private final SuspiciousIPFacade suspiciousIPFacade;
	TransactionIpValidator(SuspiciousIPFacade suspiciousIPFacade) {
		this.suspiciousIPFacade = suspiciousIPFacade;
	}

	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		String ip = request.ip();
		if(!suspiciousIPFacade.isValid(ip))
			throw new IpFormatException(ip);
		if (suspiciousIPFacade.isBlacklisted(request.ip()))
			info.add("ip");
		return nextValidation(request, info);
	}
}
