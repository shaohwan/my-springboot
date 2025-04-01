package com.demo.daniel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    private int code;
    private String message;
    private T data;
    private boolean success;

    public static <T> Response<T> success() {
        return new Response<>(200, "Operation successful", null, true);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "Operation successful", data, true);
    }

    public static <T> Response<T> success(T data, String message) {
        return new Response<>(200, message, data, true);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null, false);
    }

    public static <T> Response<T> error(int code, String message, T data) {
        return new Response<>(code, message, data, false);
    }
}
