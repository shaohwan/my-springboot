package com.demo.daniel.model;

import lombok.Data;

import java.util.List;

@Data
public class UserAddOrUpdateVO {

    private Long id;

    private String username;

    private String password;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private Boolean enabled;

    private List<Long> roleIds;
}
