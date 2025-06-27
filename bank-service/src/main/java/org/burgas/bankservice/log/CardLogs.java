package org.burgas.bankservice.log;

import lombok.Getter;

@Getter
public enum CardLogs {

    CARD_FOUND_BEFORE_DEACTIVATION("Card was found before deactivation: {}"),
    CARD_FOUND_BY_IDENTITY_ID("Card was found by identity id: {}"),
    CARD_FOUND_BY_NUMBER_VALID_CODE("Card was found by number valid and code: {}");

    private final String log;

    CardLogs(String log) {
        this.log = log;
    }

}
