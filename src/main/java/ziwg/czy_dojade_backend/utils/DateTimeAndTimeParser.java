package ziwg.czy_dojade_backend.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeAndTimeParser {
    public static LocalTime parseTime(String timeStr) {
        // Split the time string by ":" to extract hours, minutes, and seconds
        String[] timeParts = timeStr.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Adjust hours if it exceeds 23
        if (hours == 24 && minutes == 0 && seconds == 0) {
            // Treat "24:00:00" as equivalent to "00:00:00" of the next day
            return LocalTime.MIDNIGHT;
        } else if (hours >= 24) {
            // Subtract 24 hours to make it a valid time of the next day
            hours -= 24;
        }

        return LocalTime.of(hours, minutes, seconds);
    }

    public static LocalDateTime parseDateTime(LocalDateTime dateTime) {
        String pattern = "dd-MM-yy HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDateTimeStr = dateTime.format(formatter);
        return LocalDateTime.parse(formattedDateTimeStr, formatter);
    }
}
