package com.demo.daniel.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {
    @Getter
    private final int code;
    private final String message;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}