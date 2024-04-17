package pl.bgnat.antifraudsystem.domain.exceptions;

import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class IpFormatException extends RequestValidationException {
	public static final String  WRONG_IP_FORMAT_S = "Wrong IP format: %s";
	public IpFormatException(String ipAddress) {
		super(String.format(WRONG_IP_FORMAT_S, ipAddress));
	}
}
