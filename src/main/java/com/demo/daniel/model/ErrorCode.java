package com.demo.daniel.model;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "访问被禁止"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),

    // 业务错误码
    USER_NOT_EXIST(1001, "用户不存在"),
    INVALID_CREDENTIALS(1002, "用户名或密码错误"),
    USERNAME_EXISTS(1003, "用户名已存在"),

    ROLE_NOT_EXIST(1004, "角色不存在");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}