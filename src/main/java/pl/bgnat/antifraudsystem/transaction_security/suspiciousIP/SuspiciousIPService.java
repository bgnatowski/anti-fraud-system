package pl.bgnat.antifraudsystem.transaction_security.suspiciousIP;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.DuplicatedSuspiciousIPException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.SuspiciousIpAddressNotFound;
import pl.bgnat.antifraudsystem.transaction.TransactionValidator;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.request.SuspiciousIPRequest;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.response.SuspiciousIpDeleteResponse;

import java.util.List;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Service
public class SuspiciousIPService {
	private final SuspiciousIPRepository ipRepository;
	private final TransactionValidator transactionValidator;

	public SuspiciousIPService(SuspiciousIPRepository ipRepository, TransactionValidator transactionValidator) {
		this.ipRepository = ipRepository;
		this.transactionValidator = transactionValidator;
	}

	public SuspiciousIP addSuspiciousIp(SuspiciousIPRequest suspiciousIPRequest) {
		String ipAddress = suspiciousIPRequest.ip();
		if(!isValidIpAddress(ipAddress))
			throw new RequestValidationException(WRONG_JSON_FORMAT);

		if(isAlreadyInDb(ipAddress))
			throw new DuplicatedSuspiciousIPException(ipAddress);
		SuspiciousIP ip = SuspiciousIP.builder().ipAddress(ipAddress).build();
		return ipRepository.save(ip);
	}

	public SuspiciousIpDeleteResponse deleteSuspiciousIpByIpAddress(String ipAddress) {
		if(!isAlreadyInDb(ipAddress))
			throw new SuspiciousIpAddressNotFound(ipAddress);
		ipRepository.deleteByIpAddress(ipAddress);
		return new SuspiciousIpDeleteResponse(String.format("IP %s successfully removed!", ipAddress));
	}

	public List<SuspiciousIP> getAllSuspiciousIPs() {
		return ipRepository.findAll(Pageable.ofSize(100)).getContent();
	}

	private boolean isValidIpAddress(String ipAddress) {
		return transactionValidator.isValidIpAddress(ipAddress);
	}
	private boolean isAlreadyInDb(String ipAddress) {
		return ipRepository.existsByIpAddress(ipAddress);
	}
}
