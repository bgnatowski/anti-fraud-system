package pl.bgnat.antifraudsystem.utils.validator;

public class EmailValidator {
	public static boolean isValid(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(emailRegex);
	}
}
