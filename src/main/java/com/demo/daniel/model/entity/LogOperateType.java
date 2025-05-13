package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogOperateType {

    ADD(1),

    EDIT(2),

    DELETE(3),

    SEARCH(4),

    OTHER(0);

    private final int value;

    @JsonValue
    public int getValue() {
        return value;
    }
}
