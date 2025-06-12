package org.burgas.bankservice.exception;

public class WrongPinCodeException extends RuntimeException {

    public WrongPinCodeException(String message) {
        super(message);
    }
}
