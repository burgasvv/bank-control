package org.burgas.bankservice.exception;

public class EmptyIdentityPasswordException extends RuntimeException {

    public EmptyIdentityPasswordException(String message) {
        super(message);
    }
}
