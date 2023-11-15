package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that a method has been passed an incorrect period of date.
 *
 */
public class IncorrectPeriodException extends IllegalArgumentException {

    /**
     * Constructs an {@code IncorrectPeriodException} with default
     * detail message.
     */
    public IncorrectPeriodException() {
        super("Incorrect period of transactions has been passed, try again");
    }

    /**
     * Constructs an {@code IncorrectPeriodException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public IncorrectPeriodException(String message) {
        super(message);
    }
}
