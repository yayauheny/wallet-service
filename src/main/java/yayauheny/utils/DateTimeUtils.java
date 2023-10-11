package yayauheny.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * The {@link DateTimeUtils} utility class provides methods for formatting
 * date and time using specified patterns.
 */
@UtilityClass
public class DateTimeUtils {

    public DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * Formats the given {@code LocalDateTime} to a date string.
     *
     * @param dateTime The {@code LocalDateTime} to be formatted.
     * @return The formatted date string.
     */
    public String parseDate(LocalDateTime dateTime) {
        return dateTime.format(dateFormatter);
    }

    /**
     * Formats the given {@code LocalDateTime} to a time string.
     *
     * @param dateTime The {@code LocalDateTime} to be formatted.
     * @return The formatted time string.
     */
    public String parseTime(LocalDateTime dateTime) {
        return dateTime.format(timeFormatter);
    }

    /**
     * Gets the current date in the formatted string.
     *
     * @return The formatted current date string.
     */
    public String parseCurrentDate() {
        return LocalDate.now().format(dateFormatter);
    }

    /**
     * Gets the current time in the formatted string.
     *
     * @return The formatted current time string.
     */
    public String parseCurrentTime() {
        return LocalTime.now().format(timeFormatter);
    }
}


