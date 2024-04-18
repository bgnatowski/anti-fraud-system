package pl.bgnat.antifraudsystem.domain.susip;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.domain.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.domain.response.SuspiciousIpDeleteResponse;
import pl.bgnat.antifraudsystem.domain.exceptions.DuplicatedSuspiciousIPException;
import pl.bgnat.antifraudsystem.domain.exceptions.SuspiciousIpAddressNotFound;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class SuspiciousIPService {
	private final SuspiciousIPRepository ipRepository;
	SuspiciousIP addSuspiciousIp(SuspiciousIPRequest suspiciousIPRequest) {
		String ipAddress = suspiciousIPRequest.ip();
		if(isAlreadyInDb(ipAddress))
			throw new DuplicatedSuspiciousIPException(ipAddress);
		SuspiciousIP ip = SuspiciousIP.builder().ip(ipAddress).build();
		return ipRepository.save(ip);
	}


	SuspiciousIpDeleteResponse deleteSuspiciousIpByIpAddress(String ipAddress) {
		if(!isAlreadyInDb(ipAddress))
			throw new SuspiciousIpAddressNotFound(ipAddress);
		ipRepository.deleteByIp(ipAddress);
		return new SuspiciousIpDeleteResponse(String.format("IP %s successfully removed!", ipAddress));
	}

	boolean isBlacklisted(String ipAddress) {
		return isAlreadyInDb(ipAddress);
	}

	List<SuspiciousIP> getAllSuspiciousIPs() {
		return ipRepository.findAll()
				.stream()
				.sorted(Comparator.comparingLong(SuspiciousIP::getId))
				.collect(Collectors.toList());
	}

	private boolean isAlreadyInDb(String ipAddress) {
		return ipRepository.existsByIp(ipAddress);
	}
}
