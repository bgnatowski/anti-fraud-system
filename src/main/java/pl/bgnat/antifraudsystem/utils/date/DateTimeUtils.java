package pl.bgnat.antifraudsystem.utils.date;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateTimeUtils {
    private final static Clock clock = Clock.systemDefaultZone();

    public static LocalDateTime currentLocalDateTime(){
        return LocalDateTime.now(clock);
    }

    public static LocalDate currentLocalDate(){
        return LocalDate.now(clock);
    }

    public static LocalTime currentLocalTime(){
        return LocalTime.now(clock);
    }
}
