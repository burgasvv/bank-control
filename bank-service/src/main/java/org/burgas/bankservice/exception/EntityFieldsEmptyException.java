package org.burgas.bankservice.exception;

public class EntityFieldsEmptyException extends RuntimeException {

    public EntityFieldsEmptyException(String message) {
        super(message);
    }
}
