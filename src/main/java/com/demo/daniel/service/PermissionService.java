package com.demo.daniel.service;

import com.demo.daniel.entity.Permission;
import com.demo.daniel.entity.Role;
import com.demo.daniel.entity.User;
import com.demo.daniel.model.PermissionAddOrUpdateVO;
import com.demo.daniel.model.PermissionVO;
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

    public List<Permission> getMenuTreeByUsername(String username) {
        List<Permission> allPermissions;

        // 如果 username 为空或 null，返回所有权限
        if (username == null || username.trim().isEmpty()) {
            allPermissions = permissionRepository.findAll();
        } else {
            // 1. 根据用户名查询用户
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 2. 获取用户的所有角色及其权限
            allPermissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .distinct() // 去重
                    .collect(Collectors.toList());
        }

        // 3. 构建菜单树
        return buildMenuTree(allPermissions);
    }

    private List<Permission> buildMenuTree(List<Permission> permissions) {
        // 使用 Map 存储 ID 到 Permission 的映射，便于快速查找
        Map<Long, Permission> permissionMap = new HashMap<>();
        for (Permission p : permissions) {
            permissionMap.put(p.getId(), p);
            p.setChildren(new ArrayList<>()); // 初始化子节点列表
        }

        // 存储顶级菜单
        List<Permission> rootMenus = new ArrayList<>();

        // 遍历所有权限，构建树形结构
        for (Permission p : permissions) {
            Permission parent = p.getParent();
            if (parent == null) {
                // 顶级菜单
                rootMenus.add(p);
            } else {
                // 获取父节点的 ID
                Long parentId = parent.getId();
                if (parentId == null || !permissionMap.containsKey(parentId)) {
                    // 如果父节点不存在（可能被过滤掉），作为顶级节点处理
                    rootMenus.add(p);
                } else if (!parentId.equals(p.getId())) { // 防止自引用
                    // 添加到父节点的子节点列表
                    Permission parentNode = permissionMap.get(parentId);
                    parentNode.getChildren().add(p);
                }
            }
        }

        // 可选：按 orderNum 排序
        rootMenus.sort(Comparator.comparing(Permission::getOrderNum));
        for (Permission root : rootMenus) {
            root.getChildren().sort(Comparator.comparing(Permission::getOrderNum));
        }

        return rootMenus;
    }

    public void savePermission(PermissionAddOrUpdateVO permissionAddOrUpdateVO) {
        Permission permission = new Permission();
        if (permissionAddOrUpdateVO.getId() != null) {
            permission = permissionRepository.findById(permissionAddOrUpdateVO.getId())
                    .orElseThrow(() -> new RuntimeException("权限不存在"));
        }
        if (permissionAddOrUpdateVO.getParentId() != null) {
            Permission parent = permissionRepository.findById(permissionAddOrUpdateVO.getParentId())
                    .orElseThrow(() -> new RuntimeException("父权限不存在"));
            permission.setParent(parent);
        } else {
            permission.setParent(null);
        }
        BeanUtils.copyProperties(permissionAddOrUpdateVO, permission);
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

    public PermissionVO getPermissionById(Long id) {
        return permissionRepository.findById(id).map(permission -> {
                    PermissionVO permissionVO = new PermissionVO();
                    Long parentId = Optional.ofNullable(permission.getParent())
                            .map(Permission::getId)
                            .orElse(null);
                    permissionVO.setParentId(parentId);
                    BeanUtils.copyProperties(permission, permissionVO);
                    return permissionVO;
                })
                .orElseThrow(() -> new RuntimeException("权限不存在"));
    }
}