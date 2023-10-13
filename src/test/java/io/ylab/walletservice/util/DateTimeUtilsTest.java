package io.ylab.walletservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


class DateTimeUtilsTest {

    @Test
    @DisplayName("should return date from localDateTime object")
    void shouldReturnDateInStringFromLocalDateTime() {
        LocalDateTime dateTime = LocalDate.of(2022, 11, 11).atStartOfDay();

        String actualResult = DateTimeUtils.parseDate(dateTime);

        assertThat(actualResult).isEqualTo("2022.11.11");
    }

    @Test
    @DisplayName("should return time from localDateTime object")
    void shouldReturnTimeInStringFromLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 11, 11, 5, 25);

        String actualResult = DateTimeUtils.parseTime(dateTime);

        assertThat(actualResult).isEqualTo("05:25:00");
    }

    @Test
    @DisplayName("should return current date in formatted form")
    void shouldReturnCurrentDate() {
        String expected = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        String actualResult = DateTimeUtils.parseCurrentDate();

        assertThat(actualResult).isEqualTo(expected);
    }

    @Test
    @DisplayName("should return current time in formatted form")
    void shouldReturnCurrentTime() {
        String expected = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String actualResult = DateTimeUtils.parseCurrentTime();

        assertThat(actualResult).isEqualTo(expected);
    }
}