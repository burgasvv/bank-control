package org.burgas.bankservice.log;

import lombok.Getter;

@Getter
public enum IdentityLogs {

    IDENTITY_FOUND_BEFORE_ENABLE_OR_DISABLE("Identity was found before enable or disable: {}"),
    IDENTITY_FOUND_BEFORE_CHANGE_PASSWORD("identity was found before change password: {}"),
    IDENTITY_FOUND_BEFORE_DELETE("Identity was found before delete: {}"),
    IDENTITY_FOUND_BY_EMAIL("Identity was found by email: {}"),
    IDENTITY_FOUND_BY_ID("Identity was found by id: {}"),
    IDENTITY_FOUND_ALL("Identity was found in list of all: {}");

    private final String log;

    IdentityLogs(String log) {
        this.log = log;
    }

}
