package io.ylab.walletservice.exception;

import com.sun.jdi.InternalException;

/**
 * Thrown to indicate that exception occured during building receipt.
 */
public class ReceiptBuildingException extends InternalException {

    /**
     * Constructs a {@code ReceiptBuildingException} with default
     * detail message.
     */
    public ReceiptBuildingException() {
        super("Exception while printing receipt. Something gone wrong, try again");
    }

    /**
     * Constructs a {@code ReceiptBuildingException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ReceiptBuildingException(String message) {
        super(message);
    }
}


