package com.demo.daniel.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ScheduleStatus {

    PAUSE(0),

    NORMAL(1);

    private final int value;

    @JsonCreator
    public static ScheduleStatus fromValue(int value) {
        for (ScheduleStatus status : ScheduleStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ScheduleStatus value: " + value);
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
