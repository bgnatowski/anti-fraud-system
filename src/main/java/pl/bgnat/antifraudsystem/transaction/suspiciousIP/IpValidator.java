package pl.bgnat.antifraudsystem.transaction.suspiciousIP;

import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
class IpValidator {
	public boolean isValid(String ipAddress) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			return inetAddress instanceof Inet4Address;
		} catch (UnknownHostException | NullPointerException ex) {
			return false;
		}
	}
}
