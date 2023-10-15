package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that required object haven't been found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a {@code NotFoundException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code NotFoundException} with default
     * detail message.
     */
    public NotFoundException() {
        super("Object haven't been found. Try again");
    }
}


