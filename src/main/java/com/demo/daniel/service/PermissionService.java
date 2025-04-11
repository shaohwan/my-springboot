package com.demo.daniel.service;

import com.demo.daniel.model.dto.PermissionCreateDTO;
import com.demo.daniel.model.dto.PermissionUpdateDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.PermissionDetailVO;
import com.demo.daniel.model.vo.PermissionVO;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.BeanUtils;
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

    public List<Permission> getPermissionByIds(List<Long> permissionIds) {
        return permissionRepository.findAllById(permissionIds);
    }

    public List<PermissionVO> getMenuTreeByUsername(String username) {
        List<Permission> allPermissions;

        if (username == null || username.trim().isEmpty()) {
            allPermissions = permissionRepository.findAll();
        } else {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            allPermissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .distinct() // 去重
                    .collect(Collectors.toList());
        }

        // 3. 构建菜单树
        List<Permission> permissions = buildMenuTree(allPermissions);
        return permissions.stream().map(permission -> {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            return permissionVO;
        }).collect(Collectors.toList());
    }

    private List<Permission> buildMenuTree(List<Permission> permissions) {
        Map<Long, Permission> permissionMap = new HashMap<>();
        for (Permission p : permissions) {
            permissionMap.put(p.getId(), p);
            p.setChildren(new ArrayList<>()); // 初始化子节点列表
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
                } else if (!parentId.equals(p.getId())) { // 防止自引用
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

    public void savePermission(PermissionCreateDTO request) {
        Permission permission = new Permission();
        if (request.getParentId() != null) {
            Permission parent = permissionRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父权限不存在"));
            permission.setParent(parent);
        } else {
            permission.setParent(null);
        }
        BeanUtils.copyProperties(request, permission);
        permissionRepository.save(permission);
    }

    public void updatePermission(PermissionUpdateDTO request) {
        Permission permission = permissionRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("权限不存在"));
        if (request.getParentId() != null) {
            Permission parent = permissionRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父权限不存在"));
            permission.setParent(parent);
        } else {
            permission.setParent(null);
        }
        BeanUtils.copyProperties(request, permission);
        permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("权限不存在"));

        // 获取所有子权限（递归）
        List<Permission> allPermissions = new ArrayList<>();
        collectAllPermissions(permission, allPermissions);

        // 检查当前权限及其子权限是否被角色关联
        List<Permission> associatedPermissions = checkRoleAssociation(allPermissions);
        if (!associatedPermissions.isEmpty()) {
            String associatedNames = associatedPermissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("权限或其子权限（" + associatedNames + "）已被角色关联，无法删除");
        }

        // 删除权限及其子权限
        permissionRepository.delete(permission); // CascadeType.ALL 会级联删除子权限
    }

    /**
     * 递归收集所有子权限
     */
    private void collectAllPermissions(Permission permission, List<Permission> allPermissions) {
        allPermissions.add(permission);
        for (Permission child : permission.getChildren()) {
            collectAllPermissions(child, allPermissions);
        }
    }

    private List<Permission> checkRoleAssociation(List<Permission> permissions) {
        List<Permission> associatedPermissions = new ArrayList<>();
        Set<Long> permissionIds = permissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        // 查询所有角色的权限关联
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

    public PermissionDetailVO getPermissionById(Long id) {
        return permissionRepository.findById(id).map(permission -> {
                    PermissionDetailVO permissionDetailVO = new PermissionDetailVO();
                    Long parentId = Optional.ofNullable(permission.getParent())
                            .map(Permission::getId)
                            .orElse(null);
                    permissionDetailVO.setParentId(parentId);
                    BeanUtils.copyProperties(permission, permissionDetailVO);
                    return permissionDetailVO;
                })
                .orElseThrow(() -> new RuntimeException("权限不存在"));
    }
}