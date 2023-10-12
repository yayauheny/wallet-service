package io.ylab.walletservice.exception;

public class InvalidFundsException extends IllegalArgumentException {

    public InvalidFundsException() {
        super("Incorrect amount has been passed, try again");
    }

    public InvalidFundsException(String message) {
        super(message);
    }
}
