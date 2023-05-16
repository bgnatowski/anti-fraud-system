package pl.bgnat.antifraudsystem.exception.suspiciousIP;

import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;

public class SuspiciousIpAddressNotFound extends ResourceNotFoundException {

	public static final String THERE_IS_NO_IP_S_IN_DATABASE = "There is no ip: %s in database.";

	public SuspiciousIpAddressNotFound(String ipAddress) {
		super(String.format(THERE_IS_NO_IP_S_IN_DATABASE, ipAddress));
	}
}
