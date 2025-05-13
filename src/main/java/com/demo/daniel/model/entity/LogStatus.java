package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogStatus {

    FAILURE(0),

    SUCCESS(1);

    private final int value;

    @JsonCreator
    public static LogStatus fromValue(int value) {
        for (LogStatus status : LogStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid LogStatus value: " + value);
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
