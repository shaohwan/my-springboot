package com.demo.daniel.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleAddOrUpdateVO {

    private Long id;

    private String name;

    private String description;

    private List<Long> permissionIds;
}
