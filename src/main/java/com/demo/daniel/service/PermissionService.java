package com.demo.daniel.service;

import com.demo.daniel.convert.PermissionConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.PermissionUpsertDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<Permission> getMenuTree(String username) {
        List<Permission> allPermissions;

        if (StringUtils.isEmpty(username)) {
            allPermissions = permissionRepository.findAll();
        } else {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User Name " + username + " not found"));

            allPermissions = user.getSuperAdmin() ? permissionRepository.findAll() : user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

        return buildMenuTree(allPermissions);
    }

    private List<Permission> buildMenuTree(List<Permission> permissions) {
        Map<Long, Permission> permissionMap = new HashMap<>();
        for (Permission p : permissions) {
            permissionMap.put(p.getId(), p);
            p.setChildren(new ArrayList<>());
        }

        List<Permission> rootMenus = new ArrayList<>();
        for (Permission p : permissions) {
            Permission parent = p.getParent();
            if (parent == null) {
                rootMenus.add(p);
            } else {
                Long parentId = parent.getId();
                if (parentId == null || !permissionMap.containsKey(parentId)) {
                    rootMenus.add(p);
                } else if (!parentId.equals(p.getId())) {
                    Permission parentNode = permissionMap.get(parentId);
                    parentNode.getChildren().add(p);
                }
            }
        }

        rootMenus.sort(Comparator.comparing(Permission::getOrderNum));
        for (Permission root : rootMenus) {
            root.getChildren().sort(Comparator.comparing(Permission::getOrderNum));
        }

        return rootMenus;
    }

    @Transactional
    public void upsertPermission(PermissionUpsertDTO request) {
        Permission permission = Optional.ofNullable(request.getId())
                .flatMap(id -> permissionRepository.findById(id))
                .map(r -> PermissionConvert.convertToEntity(request, r))
                .orElseGet(() -> PermissionConvert.convertToEntity(request, null));

        permission.setParent(Optional.ofNullable(request.getParentId())
                .map(parentId -> permissionRepository.findById(parentId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.PERMISSION_NOT_EXIST.getCode(), "Permission ID " + parentId + " not found")))
                .orElse(null));

        permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        permissionRepository.findById(id).ifPresentOrElse(permission -> {
            List<Permission> allPermissions = new ArrayList<>();
            getPermissions(permission, allPermissions);

            List<Permission> associatedPermissions = checkRoleAssociation(allPermissions);
            if (!associatedPermissions.isEmpty()) {
                String associatedNames = associatedPermissions.stream()
                        .map(Permission::getName).distinct()
                        .collect(Collectors.joining(", "));
                throw new BusinessException(ErrorCode.PERMISSION_IN_USE.getCode()
                        , "Permission or its sub-permissions (" + associatedNames + ") are associated with roles and cannot be deleted");
            }

            permissionRepository.delete(permission);
        }, () -> {
            throw new BusinessException(ErrorCode.PERMISSION_NOT_EXIST.getCode(), "Permission ID " + id + " not found");
        });
    }

    private void getPermissions(Permission permission, List<Permission> permissions) {
        permissions.add(permission);
        for (Permission child : permission.getChildren()) {
            getPermissions(child, permissions);
        }
    }

    private List<Permission> checkRoleAssociation(List<Permission> permissions) {
        List<Permission> associatedPermissions = new ArrayList<>();
        Set<Long> permissionIds = permissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            for (Permission perm : role.getPermissions()) {
                if (permissionIds.contains(perm.getId())) {
                    associatedPermissions.add(perm);
                }
            }
        }
        return associatedPermissions;
    }

    public Permission getPermission(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PERMISSION_NOT_EXIST.getCode(), "Permission ID " + id + " not found"));
    }
}
