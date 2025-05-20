package com.demo.daniel.model.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {

    private String username;

    private String oldPassword;

    private String newPassword;
}
