package com.demo.daniel.model.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private Boolean enabled;
}
