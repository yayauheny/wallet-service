package yayauheny.exception;

import java.rmi.NoSuchObjectException;


public class NotFoundException extends NoSuchObjectException {

    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException() {
        super("Object haven't been found. Try again");
    }
}
