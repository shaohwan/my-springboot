package com.demo.daniel.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    private ApiResponse() {
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> failure() {
        return failure(ResponseCode.FAILURE);
    }

    public static <T> ApiResponse<T> failure(ResponseCode responseCode) {
        return failure(responseCode.getCode(), responseCode.getMessage());
    }

    public static <T> ApiResponse<T> failure(String message) {
        return failure(ResponseCode.FAILURE.getCode(), message);
    }

    public static <T> ApiResponse<T> failure(int statusCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(statusCode);
        response.setMessage(message);
        return response;
    }
}