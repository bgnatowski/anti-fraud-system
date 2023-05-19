package pl.bgnat.antifraudsystem.transaction.suspiciousIP;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.transaction.suspiciousIP.dto.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.transaction.suspiciousIP.dto.SuspiciousIpDeleteResponse;
import pl.bgnat.antifraudsystem.transaction.suspiciousIP.exceptions.DuplicatedSuspiciousIPException;
import pl.bgnat.antifraudsystem.transaction.suspiciousIP.exceptions.IpFormatException;
import pl.bgnat.antifraudsystem.transaction.suspiciousIP.exceptions.SuspiciousIpAddressNotFound;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
class SuspiciousIPService {
	private final SuspiciousIPRepository ipRepository;
	private final IpValidator ipValidator;

	SuspiciousIPService(SuspiciousIPRepository ipRepository,
						IpValidator ipValidator) {
		this.ipRepository = ipRepository;
		this.ipValidator = ipValidator;
	}


	SuspiciousIP addSuspiciousIp(SuspiciousIPRequest suspiciousIPRequest) {
		if(!isValidRequestJson(suspiciousIPRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		String ipAddress = suspiciousIPRequest.ip();
		checkIpFormat(ipAddress);
		if(isAlreadyInDb(ipAddress))
			throw new DuplicatedSuspiciousIPException(ipAddress);
		SuspiciousIP ip = SuspiciousIP.builder().ip(ipAddress).build();
		return ipRepository.save(ip);
	}

	boolean isValidRequestJson(SuspiciousIPRequest suspiciousIPRequest) {
		return suspiciousIPRequest.ip() != null;
	}

	SuspiciousIpDeleteResponse deleteSuspiciousIpByIpAddress(String ipAddress) {
		checkIpFormat(ipAddress);
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

	boolean isValidIpAddress(String ipAddress) {
		return ipValidator.isValid(ipAddress);
	}
	private void checkIpFormat(String ipAddress) {
		if(!isValidIpAddress(ipAddress))
			throw new IpFormatException(ipAddress);
	}

	private boolean isAlreadyInDb(String ipAddress) {
		return ipRepository.existsByIp(ipAddress);
	}
}
