package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PermissionType {
    MENU(0),

    BUTTON(1);

    private final int value;

    @JsonCreator
    public static PermissionType fromValue(int value) {
        for (PermissionType type : PermissionType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PermissionType value: " + value);
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
