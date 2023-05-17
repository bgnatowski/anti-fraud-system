package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.stereotype.Component;

@Component
public class SuspiciousIPFacade {
	private final SuspiciousIPService suspiciousIpService;
	public SuspiciousIPFacade(SuspiciousIPService suspiciousIpService) {
		this.suspiciousIpService = suspiciousIpService;
	}

	public boolean isBlacklisted(String ipAddress){
		return this.suspiciousIpService.isBlacklisted(ipAddress);
	}

	public boolean isValid(String ipAddress) {
		return suspiciousIpService.isValidIpAddress(ipAddress);
	}


}
