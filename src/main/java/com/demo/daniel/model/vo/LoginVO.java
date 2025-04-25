package com.demo.daniel.model.vo;

import lombok.Data;

@Data
public class LoginVO {

    private Long userId;

    private String username;

    private String accessToken;

    private String refreshToken;
}
