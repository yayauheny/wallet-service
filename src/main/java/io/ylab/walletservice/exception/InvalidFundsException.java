package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that a method has been passed an invalid amount for transaction.
 *
 */
public class InvalidFundsException extends IllegalArgumentException {

    /**
     * Constructs an {@code InvalidFundsException} with default
     * detail message.
     */
    public InvalidFundsException() {
        super("Incorrect amount has been passed, try again");
    }

    /**
     * Constructs an {@code InvalidFundsException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidFundsException(String message) {
        super(message);
    }
}
