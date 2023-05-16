package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuspiciousIPFacade {
	private final SuspiciousIPService suspiciousIpService;

	public SuspiciousIPFacade(SuspiciousIPService suspiciousIpService) {
		this.suspiciousIpService = suspiciousIpService;
	}

	public List<SuspiciousIP> getAllSuspiciousIps(){
		return this.suspiciousIpService.getAllSuspiciousIPs();
	}

}
