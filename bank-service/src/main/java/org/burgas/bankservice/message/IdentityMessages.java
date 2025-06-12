package org.burgas.bankservice.message;

import lombok.Getter;

@Getter
public enum IdentityMessages {

    IDENTITY_SELF_CONTROL("Identity self control detected"),
    IDENTITY_ENABLED("Identity successfully enabled"),
    IDENTITY_DISABLED("Identity successfully disabled"),
    IDENTITY_SAME_DATA("Identity already have same data"),
    IDENTITY_PASSWORD_EMPTY("identity password is empty"),
    IDENTITY_PASSWORD_CHANGED("Identity password was successfully changed"),
    IDENTITY_DELETED("Identity successfully deleted"),

    IDENTITY_EMPTY_AUTHORITY("Identity empty authority"),
    IDENTITY_EMPTY_NAME("Identity empty name"),
    IDENTITY_EMPTY_SURNAME("Identity empty surname"),
    IDENTITY_EMPTY_PATRONYMIC("Identity empty patronymic"),
    IDENTITY_EMPTY_PASSWORD("Identity empty password"),
    IDENTITY_EMPTY_PASSPORT("Identity empty passport"),
    IDENTITY_EMPTY_EMAIL("Identity empty passport"),
    IDENTITY_EMPTY_PHONE("Identity empty phone"),
    IDENTITY_EMPTY_ENABLED("Identity empty enabled"),

    IDENTITY_NOT_AUTHENTICATED("Identity not authenticated"),
    IDENTITY_NOT_AUTHORIZED("Identity not authorized"),
    IDENTITY_NOT_FOUND("Identity not found");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }
}
