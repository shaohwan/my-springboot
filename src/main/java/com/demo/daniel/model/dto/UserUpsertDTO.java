package com.demo.daniel.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserUpsertDTO {

    private Long id;

    private String username;

    private String password;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private Boolean enabled;

    private List<Long> roleIds;

    private List<Long> positionIds;
}
