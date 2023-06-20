package pl.bgnat.antifraudsystem.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
	public static boolean isValid(String phoneNumber) { //only for polish now
		if(phoneNumber==null)
			return false;
		// Remove any spaces from the phone number
		String sanitizedPhoneNumber = phoneNumber.replaceAll("\\s", "");

		// Regular expression pattern for Polish phone numbers in different formats
		String regex = "^(\\+48\\s?)?\\d{3}\\s?\\d{3}\\s?\\d{3}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sanitizedPhoneNumber);

		return matcher.matches();
	}

	public static String extractDigits(String phoneNumber) {
		// Remove any spaces from the phone number
		String sanitizedPhoneNumber = phoneNumber.replaceAll("\\s", "");

		// Remove the leading "+48" if present
		sanitizedPhoneNumber = sanitizedPhoneNumber.replaceAll("^\\+48", "");

		// Remove any non-digit characters
		sanitizedPhoneNumber = sanitizedPhoneNumber.replaceAll("\\D", "");

		return sanitizedPhoneNumber;
	}

}
