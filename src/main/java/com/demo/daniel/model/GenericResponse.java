package com.demo.daniel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {

    private int code;
    private String message;
    private T data;
    private boolean success;

    public static <T> GenericResponse<T> success() {
        return new GenericResponse<>(200, "Operation successful", null, true);
    }

    public static <T> GenericResponse<T> success(T data) {
        return new GenericResponse<>(200, "Operation successful", data, true);
    }

    public static <T> GenericResponse<T> success(T data, String message) {
        return new GenericResponse<>(200, message, data, true);
    }

    public static <T> GenericResponse<T> error(int code, String message) {
        return new GenericResponse<>(code, message, null, false);
    }

    public static <T> GenericResponse<T> error(int code, String message, T data) {
        return new GenericResponse<>(code, message, data, false);
    }
}
