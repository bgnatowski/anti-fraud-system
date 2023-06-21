package pl.bgnat.antifraudsystem.utils.validator;

public class UsernameValidator {
	public static boolean isValid(String username) {
		//od 4 do 20 znaków i zawartości: tylko litery, cyfry i znaki podkreślenia
		String usernameRegex = "^[a-zA-Z0-9_]{4,20}$";
		return username.matches(usernameRegex);
	}
}
