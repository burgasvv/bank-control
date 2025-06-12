package org.burgas.bankservice.exception;

public class SameIdentityDataException extends RuntimeException {

    public SameIdentityDataException(String message) {
        super(message);
    }
}
