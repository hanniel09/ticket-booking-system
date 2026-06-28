package com.hanniel.ticketBookingSystem.helper.date;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class DateHelperTest {

    @Test
    void toOffsetDateTime_shouldReturnNull_whenInputIsNull() {
        assertNull(DateHelper.toOffsetDateTime(null));
    }

    @Test
    void toOffsetDateTime_shouldReturnNull_whenInputIsBlank() {
        assertNull(DateHelper.toOffsetDateTime("   "));
    }

    @Test
    void toOffsetDateTime_shouldParseDateOnly_correctly() {
        OffsetDateTime result = DateHelper.toOffsetDateTime("27/06/2026");
        assertNotNull(result);
        assertEquals(2026, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(27, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());

        // America/Sao_Paulo (UTC-3) on June 27 is standard time
        assertEquals(ZoneOffset.ofHours(-3), result.getOffset());
    }

    @Test
    void toOffsetDateTime_shouldParseDateTime_correctly() {
        OffsetDateTime result = DateHelper.toOffsetDateTime("27/06/2026 15:30:42");
        assertNotNull(result);
        assertEquals(2026, result.getYear());
        assertEquals(6, result.getMonthValue());
        assertEquals(27, result.getDayOfMonth());
        assertEquals(15, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(42, result.getSecond());
        assertEquals(ZoneOffset.ofHours(-3), result.getOffset());
    }

    @Test
    void toOffsetDateTime_shouldThrowException_whenFormatIsUnsupported() {
        assertThrows(IllegalArgumentException.class, () -> DateHelper.toOffsetDateTime("2026-06-27"));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.toOffsetDateTime("27-06-2026"));
        assertThrows(IllegalArgumentException.class, () -> DateHelper.toOffsetDateTime("27/06/2026 15:30"));
    }

    @Test
    void toBrDateString_shouldFormatOffsetDateTime_correctly() {
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 6, 27, 15, 30, 42, 0, ZoneOffset.ofHours(-3));
        String result = DateHelper.toBrDateString(dateTime);
        assertEquals("27/06/2026", result);
    }

    @Test
    void toBrDateString_shouldReturnNull_whenInputIsNull() {
        assertNull(DateHelper.toBrDateString(null));
    }

    @Test
    void toBrDateTimeString_shouldFormatOffsetDateTime_correctly() {
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 6, 27, 15, 30, 42, 0, ZoneOffset.ofHours(-3));
        String result = DateHelper.toBrDateTimeString(dateTime);
        assertEquals("27/06/2026 15:30:42", result);
    }

    @Test
    void toBrDateTimeString_shouldReturnNull_whenInputIsNull() {
        assertNull(DateHelper.toBrDateTimeString(null));
    }
}
