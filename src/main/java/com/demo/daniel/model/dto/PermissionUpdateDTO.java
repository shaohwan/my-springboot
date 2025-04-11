package com.demo.daniel.model.dto;

import com.demo.daniel.model.entity.PermissionType;
import lombok.Data;

@Data
public class PermissionUpdateDTO {

    private Long id;

    private String name;

    private String code;

    private PermissionType type;

    private String path;

    private String component;

    private String icon;

    private Long parentId;

    private Integer orderNum;
}
