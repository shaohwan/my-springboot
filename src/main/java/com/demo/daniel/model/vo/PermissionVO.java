package com.demo.daniel.model.vo;

import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.PermissionType;
import lombok.Data;

import java.util.List;

@Data
public class PermissionVO {

    private Long id;

    private String name;

    private String code;

    private PermissionType type;

    private String url;

    private String icon;

    private Long parentId;

    private List<Permission> children;

    private Integer orderNum;
}
