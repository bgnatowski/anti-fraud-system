package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class TransactionValidator {
	private static final int CHECKSUM_DIGIT_POSITION = 15;
	private static final int CARD_NUMBER_LENGTH = 16;
	public boolean isValidCardNumber(String number) {
		if (number.length() < CARD_NUMBER_LENGTH) return false;

		boolean isOddPosition = true;
		int sum = 0;
		int checkSum = Integer.parseInt(Character.toString((number.charAt(CHECKSUM_DIGIT_POSITION))));

		for(int i = 0; i < number.length()-1; i++){
			int digit = Integer.parseInt(Character.toString((number.charAt(i))));
			int multipliedOddPositionDigitBy2 = (isOddPosition ? 2 : 1) * digit;
			int substracted9FromOver9Number = multipliedOddPositionDigitBy2 - (multipliedOddPositionDigitBy2 > 9 ? 9 : 0);
			sum += substracted9FromOver9Number;
			isOddPosition = !isOddPosition;
		}
		return (sum+checkSum) % 10 == 0;
	}

	public boolean isValidIpAddress(String ipAddress) {
		try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			return inetAddress instanceof Inet4Address;
		} catch (UnknownHostException | NullPointerException ex) {
			return false;
		}
	}



}
