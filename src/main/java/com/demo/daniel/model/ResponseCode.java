package com.demo.daniel.model;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(200, "Success"),
    FAILURE(500, "Operation failed"),
    BAD_REQUEST(400, "Invalid request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    CONFLICT(409, "Resource conflict"),
    INTERNAL_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}