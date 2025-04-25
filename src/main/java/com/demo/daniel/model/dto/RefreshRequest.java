package com.demo.daniel.model.dto;

import lombok.Data;

@Data
public class RefreshRequest {

    private String username;

    private String refreshToken;
}
