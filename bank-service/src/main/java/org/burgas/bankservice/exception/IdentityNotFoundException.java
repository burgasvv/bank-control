package org.burgas.bankservice.exception;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message);
    }
}
