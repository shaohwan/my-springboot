package com.demo.daniel.exception;

public class BusinessException extends BaseException {
    public BusinessException(int code, String message) {
        super(code, message);
    }
}
