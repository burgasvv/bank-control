package org.burgas.bankservice.log;

import lombok.Getter;

@Getter
public enum OperationLogs {

    OPERATION_FOUND_ALL("Operation was found in list of all: {}");

    private final String log;

    OperationLogs(String log) {
        this.log = log;
    }

}
