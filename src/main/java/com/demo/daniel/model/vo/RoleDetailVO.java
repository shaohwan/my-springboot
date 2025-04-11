package com.demo.daniel.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleDetailVO {

    private Long id;

    private String name;

    private String description;

    private List<Long> permissionIds;
}
