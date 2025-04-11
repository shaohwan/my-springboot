package com.demo.daniel.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleCreateDTO {

    private Long id;

    private String name;

    private String description;

    private List<Long> permissionIds;
}
