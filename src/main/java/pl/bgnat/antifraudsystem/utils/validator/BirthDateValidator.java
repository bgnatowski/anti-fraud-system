package pl.bgnat.antifraudsystem.utils.validator;

import java.time.LocalDate;
import java.time.Period;

public class BirthDateValidator {
	public static final int AGE_OF_CONSENT = 18;
	public static boolean isValid(LocalDate birthDate, LocalDate currentDate) {
		// Sprawdzanie wieku użytkownika
		Period age = Period.between(birthDate, currentDate);
		int years = age.getYears();

		return years >= AGE_OF_CONSENT;
	}
}
