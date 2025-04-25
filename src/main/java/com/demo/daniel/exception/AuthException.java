package com.demo.daniel.exception;

public class AuthException extends BaseException {
    public AuthException(int code, String message) {
        super(code, message);
    }
}
