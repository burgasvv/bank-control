package org.burgas.bankservice.exception;

public class IdentityNotAuthenticatedException extends RuntimeException {

    public IdentityNotAuthenticatedException(String message) {
        super(message);
    }
}
