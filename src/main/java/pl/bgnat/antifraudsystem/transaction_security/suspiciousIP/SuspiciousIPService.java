package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.DuplicatedSuspiciousIPException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.SuspiciousIpAddressNotFound;
import pl.bgnat.antifraudsystem.transaction.TransactionValidator;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.response.SuspiciousIpDeleteResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
class SuspiciousIPService {
	private final SuspiciousIPRepository ipRepository;
	private final TransactionValidator transactionValidator;

	SuspiciousIPService(SuspiciousIPRepository ipRepository, TransactionValidator transactionValidator) {
		this.ipRepository = ipRepository;
		this.transactionValidator = transactionValidator;
	}

	SuspiciousIP addSuspiciousIp(SuspiciousIPRequest suspiciousIPRequest) {
		if(!isValidRequestJson(suspiciousIPRequest))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		String ipAddress = suspiciousIPRequest.ip();
		if(!isValidIpAddress(ipAddress))
			throw new IpFormatException(ipAddress);
		if(isAlreadyInDb(ipAddress))
			throw new DuplicatedSuspiciousIPException(ipAddress);
		SuspiciousIP ip = SuspiciousIP.builder().ipAddress(ipAddress).build();
		return ipRepository.save(ip);
	}

	boolean isValidRequestJson(SuspiciousIPRequest suspiciousIPRequest) {
		return suspiciousIPRequest.ip() != null;
	}

	SuspiciousIpDeleteResponse deleteSuspiciousIpByIpAddress(String ipAddress) {
		if(!isAlreadyInDb(ipAddress))
			throw new SuspiciousIpAddressNotFound(ipAddress);
		ipRepository.deleteByIpAddress(ipAddress);
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
	private boolean isValidIpAddress(String ipAddress) {
		return transactionValidator.isValidIpAddress(ipAddress);
	}

	private boolean isAlreadyInDb(String ipAddress) {
		return ipRepository.existsByIpAddress(ipAddress);
	}
}
