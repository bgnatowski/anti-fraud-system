package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.SuspiciousIPFacade;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class TransactionIpValidator extends Validator {
	private final SuspiciousIPFacade suspiciousIPFacade;

	public TransactionIpValidator(SuspiciousIPFacade suspiciousIPFacade) {
		this.suspiciousIPFacade = suspiciousIPFacade;
	}

	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		String ip = request.ip();
		if(!isValid(ip))
			throw new IpFormatException(ip);
		if (suspiciousIPFacade.isBlacklisted(request.ip()))
			info.add("ip");
		return nextValidation(request, info);
	}

	private boolean isValid(String ipAddress) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			return inetAddress instanceof Inet4Address;
		} catch (UnknownHostException | NullPointerException ex) {
			return false;
		}
	}
}
