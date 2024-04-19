package pl.bgnat.antifraudsystem.utils.date;

import java.time.*;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {
    public static final int AGE_OF_CONSENT = 18;
    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private final static Clock clock = Clock.systemDefaultZone();

    public static LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now(clock);
    }

    public static LocalDate currentLocalDate() {
        return LocalDate.now(clock);
    }

    public static LocalTime currentLocalTime() {
        return LocalTime.now(clock);
    }

    public static boolean isAdult(LocalDate birthDate, LocalDate currentDate) {
        // Sprawdzanie wieku użytkownika
        Period age = Period.between(birthDate, currentDate);
        int years = age.getYears();

        return years >= AGE_OF_CONSENT;
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static LocalDate parseLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
    }
}
