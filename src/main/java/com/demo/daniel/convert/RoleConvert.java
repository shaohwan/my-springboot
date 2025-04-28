package com.demo.daniel.convert;

import com.demo.daniel.model.dto.RoleUpsertDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.vo.RoleVO;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import java.util.stream.Collectors;

public class RoleConvert {

    private RoleConvert() {
    }

    public static RoleVO convertToVO(Role role) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        roleVO.setPermissionIds(Optional.ofNullable(role.getPermissions())
                .map(permissions -> permissions.stream().map(Permission::getId).collect(Collectors.toList())).orElse(null));
        return roleVO;
    }

    public static Role convertToEntity(RoleUpsertDTO dto, Role role) {
        Role newRole = new Role();
        if (role != null) {
            newRole = role;
        }
        BeanUtils.copyProperties(dto, newRole);
        return newRole;
    }
}
