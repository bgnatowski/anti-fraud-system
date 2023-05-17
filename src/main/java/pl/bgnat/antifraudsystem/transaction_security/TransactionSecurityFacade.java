package pl.bgnat.antifraudsystem.transaction_security;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.StolenCardFacade;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.SuspiciousIPFacade;


@Component
public class TransactionSecurityFacade {
	private final StolenCardFacade stolenCardFacade;
	private final SuspiciousIPFacade suspiciousIPFacade;
	public TransactionSecurityFacade(StolenCardFacade stolenCardFacade,
									 SuspiciousIPFacade suspiciousIPFacade) {
		this.stolenCardFacade = stolenCardFacade;
		this.suspiciousIPFacade = suspiciousIPFacade;
	}

	public boolean isBlacklistedCard(String data) {
		return stolenCardFacade.isBlacklisted(data);
	}

	public boolean isBlacklistedIp(String data) {
		return suspiciousIPFacade.isBlacklisted(data);
	}

	public boolean isValidCardNumber(String number) {
		return stolenCardFacade.isValid(number);
	}

	public boolean isValidIpAddress(String ipAddress) {
		return suspiciousIPFacade.isValid(ipAddress);
	}
}