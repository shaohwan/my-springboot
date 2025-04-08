package com.demo.daniel.model;

import com.demo.daniel.entity.PermissionType;
import lombok.Data;

@Data
public class PermissionAddOrUpdateVO {

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
