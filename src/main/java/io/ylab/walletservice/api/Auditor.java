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
     * Logs a message.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        String formattedMessage = String.format("[%s] %s", LocalDateTime.now(), message);
        LOG.add(formattedMessage);
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
    public void remove() {
        LOG.remove();
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

