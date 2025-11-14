package org.genc.sneakoapp.cartmanagementservice.exception;

public class InvalidCartItemException extends RuntimeException {
    public InvalidCartItemException(String message) {
        super(message);
    }
}
