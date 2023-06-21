package pl.bgnat.antifraudsystem.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
	public static boolean isValid(String phoneNumber) { //only for polish now
		if (phoneNumber == null)
			return false;

		// Usuń spacje z numeru telefonu
		String sanitizedPhoneNumber = phoneNumber.replaceAll("\\s", "");

		// Regularne wyrażenie dla polskich numerów telefonów bez prefiksu +48
		String regex = "^(?:\\d{9}|\\d{3}\\s\\d{3}\\s\\d{3})$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sanitizedPhoneNumber);

		return matcher.matches();
	}

}
