package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogLoginOperation {

    LOGIN_SUCCESS(0),

    LOGOUT_SUCCESS(1),

    ACCOUNT_FAILURE(2);

    private final int value;

    @JsonValue
    public int getValue() {
        return value;
    }
}
