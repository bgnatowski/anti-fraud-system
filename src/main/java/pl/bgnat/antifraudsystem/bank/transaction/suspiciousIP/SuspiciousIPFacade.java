package pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP.dto.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.bank.transaction.suspiciousIP.dto.SuspiciousIpDeleteResponse;

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

	public String blacklist(String ip){
		SuspiciousIPRequest request = new SuspiciousIPRequest(ip);
		SuspiciousIP suspiciousIP = suspiciousIpService.addSuspiciousIp(request);
		return suspiciousIP.toString();
	}

	public String delete(String ip){
		SuspiciousIpDeleteResponse suspiciousIpDeleteResponse = suspiciousIpService.deleteSuspiciousIpByIpAddress(ip);
		return suspiciousIpDeleteResponse.status();
	}

}

