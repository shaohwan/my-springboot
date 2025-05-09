package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogLoginStatusType {

    FAILURE(0),

    SUCCESS(1);

    private final int value;

    @JsonValue
    public int getValue() {
        return value;
    }
}
