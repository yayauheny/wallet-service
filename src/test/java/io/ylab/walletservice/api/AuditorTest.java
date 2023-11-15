package io.ylab.walletservice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.assertj.core.api.Assertions.*;

class AuditorTest {

    @Test
    @DisplayName("should log message and return when needed")
    void shouldLogMessageCorrectly() {
        String message = "log";

        Auditor.log(message);

        String actualResult = Auditor.poll();

        assertThat(actualResult).isNotEmpty().contains(message);
    }

    @Test
    @DisplayName("should remove messages from log correctly")
    void shouldClearLogCorrectly() {
        String[] messages = {"a1", "a2", "a3"};

        Auditor.log(messages);
        Auditor.clear();

        Queue<String> actualResult = Auditor.getLog();

        assertThat(actualResult).isEmpty();
    }
}