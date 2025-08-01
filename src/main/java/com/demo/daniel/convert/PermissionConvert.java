package com.demo.daniel.convert;

import com.demo.daniel.model.dto.PermissionUpsertDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.vo.PermissionVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PermissionConvert {

    private PermissionConvert() {
    }

    public static PermissionVO convertToVO(Permission permission) {
        PermissionVO permissionVO = new PermissionVO();
        BeanUtils.copyProperties(permission, permissionVO);

        permissionVO.setParentId(Optional.ofNullable(permission.getParent()).map(Permission::getId).orElse(null));

        List<PermissionVO> children = permission.getChildren().stream()
                .map(PermissionConvert::convertToVO)
                .collect(Collectors.toList());
        permissionVO.setChildren(children);
        return permissionVO;
    }

    public static Permission convertToEntity(PermissionUpsertDTO dto, Permission permission) {
        Permission newPermission = new Permission();
        if (permission != null)
            newPermission = permission;
        BeanUtils.copyProperties(dto, newPermission);
        return newPermission;
    }
}
