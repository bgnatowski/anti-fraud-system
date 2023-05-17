package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction_security.TransactionSecurityFacade;

@Component
class TransactionValidator {
	private final TransactionSecurityFacade transactionSecurityFacade;
	TransactionValidator(TransactionSecurityFacade transactionSecurityFacade) {
		this.transactionSecurityFacade = transactionSecurityFacade;
	}

	boolean isValidCardNumber(String number) {
		return transactionSecurityFacade.isValidCardNumber(number);
	}

	boolean isValidIpAddress(String ipAddress) {
		return transactionSecurityFacade.isValidIpAddress(ipAddress);
	}

	boolean isBlacklistedCard(String cardNumber) {
		return transactionSecurityFacade.isBlacklistedCard(cardNumber);
	}
	boolean isBlacklistedIP(String ip) {
		return transactionSecurityFacade.isBlacklistedIp(ip);
	}
}
