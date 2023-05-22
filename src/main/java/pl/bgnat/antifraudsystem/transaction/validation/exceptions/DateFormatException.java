package pl.bgnat.antifraudsystem.transaction.validation.exceptions;


import pl.bgnat.antifraudsystem.exception.RequestValidationException;

public class DateFormatException extends RequestValidationException {

	public static final String INVALID_DATE_FORMAT_OF_S = "Invalid date format of: %s";

	public DateFormatException(String date) {
		super(String.format(INVALID_DATE_FORMAT_OF_S, date));
	}
}
