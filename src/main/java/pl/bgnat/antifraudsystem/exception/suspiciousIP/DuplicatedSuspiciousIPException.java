package pl.bgnat.antifraudsystem.exception.suspiciousIP;

import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;

public class DuplicatedSuspiciousIPException extends DuplicateResourceException {
	public static final String IP_S_IS_ALREADY_SUSPICIOUS = "Ip: %s is already suspicious";

	public DuplicatedSuspiciousIPException(String ipAddress) {
		super(String.format(IP_S_IS_ALREADY_SUSPICIOUS, ipAddress));
	}
}
