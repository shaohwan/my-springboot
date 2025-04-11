package com.demo.daniel.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateDTO {

    private Long id;

    private String name;

    private String description;

    private List<Long> permissionIds;
}
