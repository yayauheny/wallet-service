package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that incorrect data have been detected during validation.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs an {@code ValidationException} with default
     * detail message.
     */
    public ValidationException() {
        super("Exception during validation");
    }

    /**
     * Constructs a {@code ValidationException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ValidationException(String message) {
        super(message);
    }
}
