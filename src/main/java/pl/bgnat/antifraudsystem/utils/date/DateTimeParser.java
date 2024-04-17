package pl.bgnat.antifraudsystem.utils.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeParser {
	public static LocalDateTime parseDate(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		return LocalDateTime.parse(stringDate, formatter);
	}

	public static LocalDateTime getOneHourAgoDateFromString(String date) {
		return parseDate(date).minus(1, ChronoUnit.HOURS);
	}
}
