package org.burgas.bankservice.message;

import lombok.Getter;

@Getter
public enum CardMessages {

    CARD_NOT_FOUND("Card not found"),
    YEARS_EMPTY("Years data are empty"),
    PAYMENT_SYSTEM_EMPTY("Payment system data is empty"),
    CARD_TYPE_EMPTY("Card type data is empty");

    private final String message;

    CardMessages(String message) {
        this.message = message;
    }

}
