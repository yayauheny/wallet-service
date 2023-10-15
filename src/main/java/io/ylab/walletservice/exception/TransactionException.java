package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that unexpected exception caused during transaction processing.
 *
 */
public class TransactionException extends RuntimeException {

    /**
     * Constructs a {@code TransactionException} with default
     * detail message.
     */
    public TransactionException(Throwable cause) {
        super("Exception while processing transaction. Try again", cause);
    }

    /**
     * Constructs a {@code TransactionException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public TransactionException(String message) {
        super(message);
    }
}


