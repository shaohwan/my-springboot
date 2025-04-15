package com.demo.daniel.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    private ApiResponse() {
    }

    public static <T> ApiResponse<T> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setMessage(ErrorCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error() {
        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static <T> ApiResponse<T> error(int errorCode, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(errorCode);
        response.setMessage(message);
        return response;
    }
}