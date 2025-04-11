package com.demo.daniel.model.vo;

import com.demo.daniel.model.entity.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDetailVO {

    private Long id;

    private String username;

    private String password;

    private String realName;

    private String email;

    private String phone;

    private String avatar;

    private Boolean enabled;

    private Set<Role> roles = new HashSet<>();
}
