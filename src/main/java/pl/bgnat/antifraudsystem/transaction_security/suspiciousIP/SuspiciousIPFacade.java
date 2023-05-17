package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction_security.validation.SecurityValidator;

@Component
public class SuspiciousIPFacade {
	private final SuspiciousIPService suspiciousIpService;
	private final SecurityValidator<String> ipValidator;

	public SuspiciousIPFacade(SuspiciousIPService suspiciousIpService,
							  @Qualifier("IpValidator") SecurityValidator<String> ipValidator) {
		this.suspiciousIpService = suspiciousIpService;
		this.ipValidator = ipValidator;
	}

	public boolean isBlacklisted(String ipAddress){
		return this.suspiciousIpService.isBlacklisted(ipAddress);
	}

	public boolean isValid(String ipAddress) {
		return ipValidator.isValid(ipAddress);
	}
}
