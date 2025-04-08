package com.demo.daniel.model;

import com.demo.daniel.entity.PermissionType;
import lombok.Data;

@Data
public class PermissionVO {
    private Long id;
    private String name;
    private String code; // 可为空
    private PermissionType type; // MENU 或 BUTTON
    private String path; // 仅 MENU 使用
    private String component; // 仅 MENU 使用
    private String icon; // 仅 MENU 使用
    private Long parentId; // 父权限 ID
    private Integer orderNum;
}
