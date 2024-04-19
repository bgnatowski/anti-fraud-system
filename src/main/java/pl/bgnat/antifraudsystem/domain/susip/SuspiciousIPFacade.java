package pl.bgnat.antifraudsystem.domain.susip;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.domain.response.SuspiciousIpDeleteResponse;

@Component
@RequiredArgsConstructor
public class SuspiciousIPFacade {
    private final SuspiciousIPService suspiciousIpService;
    public boolean isBlacklisted(String ipAddress) {
        return this.suspiciousIpService.isBlacklisted(ipAddress);
    }

    public String blacklist(String ip) {
        SuspiciousIPRequest request = new SuspiciousIPRequest(ip);
        SuspiciousIP suspiciousIP = suspiciousIpService.addSuspiciousIp(request);
        return suspiciousIP.toString();
    }

    public String delete(String ip) {
        SuspiciousIpDeleteResponse suspiciousIpDeleteResponse = suspiciousIpService.deleteSuspiciousIpByIpAddress(ip);
        return suspiciousIpDeleteResponse.status();
    }

}

