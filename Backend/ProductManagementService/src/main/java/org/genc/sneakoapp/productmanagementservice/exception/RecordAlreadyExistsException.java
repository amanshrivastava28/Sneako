package org.genc.sneakoapp.productmanagementservice.exception;

public class RecordAlreadyExistsException extends RuntimeException {

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
