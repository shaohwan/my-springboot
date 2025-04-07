package com.demo.daniel.service;

import com.demo.daniel.entity.Permission;
import com.demo.daniel.entity.User;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getMenuTreeByUsername(String username) {
        // 1. 根据用户名查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 获取用户的所有角色及其权限
        List<Permission> allPermissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .distinct() // 去重
                .collect(Collectors.toList());

        // 3. 构建菜单树
        return buildMenuTree(allPermissions);
    }

    private List<Permission> buildMenuTree(List<Permission> permissions) {
        // 找出所有顶级菜单（parent 为 null）
        List<Permission> rootMenus = permissions.stream()
                .filter(p -> p.getParent() == null)
                .collect(Collectors.toList());

        // 递归设置子菜单
        for (Permission root : rootMenus) {
            root.setChildren(getChildren(root, permissions));
        }

        return rootMenus;
    }

    private List<Permission> getChildren(Permission parent, List<Permission> permissions) {
        List<Permission> children = permissions.stream()
                .filter(p -> parent.equals(p.getParent()))
                .collect(Collectors.toList());

        // 递归设置子节点的子节点
        for (Permission child : children) {
            child.setChildren(getChildren(child, permissions));
        }

        return children;
    }
}
