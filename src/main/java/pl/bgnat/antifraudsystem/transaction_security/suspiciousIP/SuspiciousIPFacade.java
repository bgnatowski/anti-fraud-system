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

	public String add(String ip){
		SuspiciousIPRequest request = new SuspiciousIPRequest(ip);
		SuspiciousIP suspiciousIP = suspiciousIpService.addSuspiciousIp(request);
		return suspiciousIP.toString();
	}

	public String delete(String ip){
		SuspiciousIpDeleteResponse suspiciousIpDeleteResponse = suspiciousIpService.deleteSuspiciousIpByIpAddress(ip);
		return suspiciousIpDeleteResponse.status();
	}

}
