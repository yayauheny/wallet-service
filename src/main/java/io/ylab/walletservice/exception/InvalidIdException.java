package io.ylab.walletservice.exception;

/**
 * Thrown to indicate that a method has been passed an invalid id.
 */
public class InvalidIdException extends IllegalArgumentException {

    /**
     * Constructs an {@code InvalidIdException} with default
     * detail message.
     */
    public InvalidIdException() {
        super("Invalid argument have been passed as id parameter");
    }

    /**
     * Constructs an {@code InvalidIdException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidIdException(String message) {
        super(message);
    }
}



