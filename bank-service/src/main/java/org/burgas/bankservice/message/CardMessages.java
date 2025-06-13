package org.burgas.bankservice.message;

import lombok.Getter;

@Getter
public enum CardMessages {

    CARD_WRONG_STATUS("This card status already set"),
    CARD_ACTIVATED("Card successfully activated"),
    CARD_DEACTIVATED("Card successfully deactivated"),
    SENDER_CARD_NOT_ENABLED("Sender card not enabled"),
    RECIPIENT_CARD_NOT_ENABLED("Recipient card not enabled"),
    CARD_NOT_ENABLED("Card not enabled"),
    NULL_MONEY_AMOUNT("Null or 0 money amount for operation"),
    TRANSFER_OPERATION_SUCCESSFUL("Transfer operation successful"),
    NOT_ENOUGH_MONEY("Not enough money for withdraw operation"),
    DEPOSIT_SUCCESS("Deposit successfully done"),
    WITHDRAW_SUCCESS("Withdraw successfully done"),
    WRONG_PIN_CODE("Wrong pin code, it must be 4 symbols"),
    WRONG_PIN("Wrong pin code"),
    PIN_EMPTY("Pin data empty"),
    CARD_NOT_FOUND("Card not found"),
    YEARS_EMPTY("Years data are empty"),
    PAYMENT_SYSTEM_EMPTY("Payment system data is empty"),
    CARD_TYPE_EMPTY("Card type data is empty");

    private final String message;

    CardMessages(String message) {
        this.message = message;
    }

}
