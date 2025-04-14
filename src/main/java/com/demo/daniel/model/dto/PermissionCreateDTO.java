package com.demo.daniel.model.dto;

import com.demo.daniel.model.entity.PermissionType;
import lombok.Data;

@Data
public class PermissionCreateDTO {

    private Long id;

    private String name;

    private String code;

    private PermissionType type;

    private String url;

    private String icon;

    private Long parentId;

    private Integer orderNum;
}
