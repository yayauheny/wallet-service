package io.ylab.walletservice.api;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;


/**
 * The {@code Auditor} class for logging actions.
 */
@UtilityClass
public class Auditor {

    /**
     * The queue to store log messages.
     */
    private final Queue<String> LOG = new ArrayDeque<>();

    /**
     * Logs a messages.
     *
     * @param messages The messages to log.
     */
    public void log(String... messages) {
        for (String message : messages) {
            String formattedMessage = String.format("[%s] %s", LocalDateTime.now(), message);
            LOG.add(formattedMessage);
        }
    }

    /**
     * Polls and retrieves the first log message from the queue.
     *
     * @return The first log message, or {@code null} if the queue is empty.
     */
    public String poll() {
        return LOG.poll();
    }

    /**
     * Removes the first log message from the queue.
     */
    public void clear() {
        LOG.clear();
    }

    /**
     * Gets the entire log queue.
     *
     * @return The log queue.
     */
    public Queue<String> getLog() {
        return LOG;
    }
}

