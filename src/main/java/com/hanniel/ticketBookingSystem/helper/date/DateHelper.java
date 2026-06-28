package com.hanniel.ticketBookingSystem.helper.date;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public final class DateHelper {

    private static final ZoneId BR_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter BR_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter BR_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private DateHelper() {

    }

    public static OffsetDateTime toOffsetDateTime(String dateStr) {
        log.info("Transforming date string: {}", dateStr);

        if (dateStr == null || dateStr.isBlank()) {
            log.debug("Date string is null or blank, returning null");
            return null;
        }

        String trimmed = dateStr.trim();
        try {
            OffsetDateTime result;
            if (trimmed.length() == 10) {
                LocalDate localDate = LocalDate.parse(trimmed, BR_DATE_FORMATTER);
                result = localDate.atStartOfDay(BR_ZONE).toOffsetDateTime();
                log.info("Successfully parsed date string '{}' to OffsetDateTime '{}'", dateStr, result);
                return result;
            } else if (trimmed.contains(" ") && trimmed.length() == 19) {
                LocalDateTime localDateTime = LocalDateTime.parse(trimmed, BR_DATE_TIME_FORMATTER);
                result = localDateTime.atZone(BR_ZONE).toOffsetDateTime();
                log.info("Successfully parsed date-time string '{}' to OffsetDateTime '{}'", dateStr, result);
                return result;
            } else {
                throw new IllegalArgumentException("Unsupported date format: " + dateStr 
                        + ". Expected formats are 'dd/MM/yyyy' or 'dd/MM/yyyy HH:mm:ss'");
            }
        } catch (DateTimeParseException | IllegalArgumentException e) {
            log.error("Failed to parse date string '{}': {}", dateStr, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to parse date: " + dateStr 
                    + ". Expected format: 'dd/MM/yyyy' or 'dd/MM/yyyy HH:mm:ss'", e);
        }
    }

    public static String toBrDateString(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.atZoneSameInstant(BR_ZONE).format(BR_DATE_FORMATTER);
    }

    /**
     * Formats an OffsetDateTime back to a Brazilian date-time string (dd/MM/yyyy HH:mm:ss).
     *
     * @param offsetDateTime the OffsetDateTime to format
     * @return the formatted date-time string, or null if input is null
     */
    public static String toBrDateTimeString(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.atZoneSameInstant(BR_ZONE).format(BR_DATE_TIME_FORMATTER);
    }
}
