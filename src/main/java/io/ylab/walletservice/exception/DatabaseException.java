package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that exception with database occured.
 */
public class DatabaseException extends Exception {

    /**
     * Constructs an {@code DatabaseException} with default
     * detail message.
     */
    public DatabaseException() {
        super("Exception during database work, try again");
    }

    /**
     * Constructs an {@code DatabaseException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param message the detail message.
     * @param cause   the exception cause.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
