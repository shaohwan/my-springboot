package com.demo.daniel.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
@Slf4j
public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    /**
     * Parses a date string (yyyy-MM-dd) to LocalDateTime, assuming start of day (00:00:00) for startTime
     * or end of day (23:59:59.999) for endTime.
     *
     * @param dateStr   the date string to parse
     * @param isEndTime whether to parse as end of day
     * @return LocalDateTime if valid, null otherwise
     */
    public static LocalDateTime parseDate(String dateStr, boolean isEndTime) {
        if (!StringUtils.hasText(dateStr)) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(dateStr, DEFAULT_FORMATTER);
            return isEndTime ? localDate.atTime(LocalTime.MAX) : localDate.atStartOfDay();
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date: {}, error: {}", dateStr, e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Formats a LocalDateTime to string with the default pattern (yyyy-MM-dd).
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted string or empty string if dateTime is null
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.toLocalDate().format(DEFAULT_FORMATTER);
    }

    /**
     * Validates if a date string matches the default pattern (yyyy-MM-dd).
     *
     * @param dateStr the date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return false;
        }
        try {
            LocalDate.parse(dateStr, DEFAULT_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}, error: {}", dateStr, e.getLocalizedMessage());
            return false;
        }
    }
}
