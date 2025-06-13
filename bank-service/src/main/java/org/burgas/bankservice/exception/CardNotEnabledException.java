package org.burgas.bankservice.exception;

public class CardNotEnabledException extends RuntimeException {

    public CardNotEnabledException(String message) {
        super(message);
    }
}
