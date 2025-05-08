package com.demo.daniel.init;

import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.PermissionType;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile({"dev"})
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private static final String[] BUTTON_ACTIONS = {"add", "edit", "delete", "search", "reset"};
    private static final String[] MENU_NAMES = {"用户管理", "角色管理", "菜单管理"};
    private static final String[] MENU_CODES = {"user", "role", "permission"};
    private static final String[] MENU_URLS = {"auth/user/index", "auth/role/index", "auth/permission/index"};

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking if data initialization is needed...");

        if (permissionRepository.count() == 0 && roleRepository.count() == 0 && userRepository.count() == 0) {
            log.info("No data found, starting initialization...");
            Set<Permission> allPermissions = new HashSet<>();
            initializePermissions(allPermissions);

            Set<Role> roles = new HashSet<>();
            initializeRoles(allPermissions, roles);

            initializeUsers(roles);
            log.info("Data initialization completed.");
        } else {
            log.info("Data already exists, skipping initialization.");
        }
    }

    private void initializePermissions(Set<Permission> allPermissions) {
        Permission root = createPermission("权限管理", PermissionType.MENU, null, 0, null);
        permissionRepository.save(root);
        allPermissions.add(root);

        for (int i = 0; i < MENU_NAMES.length; i++) {
            Permission menu = createPermission(MENU_NAMES[i], PermissionType.MENU, MENU_URLS[i], i, root);
            allPermissions.add(menu);
            permissionRepository.save(menu);

            for (int j = 0; j < BUTTON_ACTIONS.length; j++) {
                Permission button = createPermission(
                        getButtonName(BUTTON_ACTIONS[j]),
                        PermissionType.BUTTON,
                        MENU_CODES[i] + ":" + BUTTON_ACTIONS[j],
                        j,
                        menu
                );
                allPermissions.add(button);
                permissionRepository.save(button);
            }
        }
    }

    private void initializeRoles(Set<Permission> allPermissions, Set<Role> roles) {
        Role adminRole = createRole("管理员", "管理员", allPermissions);
        roles.add(adminRole);

        Role commonRole = createRole("普通用户", "普通用户", allPermissions);
        roles.add(commonRole);
        roleRepository.saveAll(roles);
    }

    private void initializeUsers(Set<Role> roles) {
        User adminUser = createUser(
                "daniel",
                "Daniel Wang",
                "123456",
                Boolean.FALSE,
                "daniel@qq.com",
                "13913133777",
                roles
        );

        User superAdmin = createUser(
                "admin",
                "admin",
                "admin",
                Boolean.TRUE,
                "admin@qq.com",
                "13913133666",
                null
        );
        userRepository.saveAll(List.of(adminUser, superAdmin));
    }

    private Permission createPermission(String name, PermissionType type, String codeOrUrl, int orderNum, Permission parent) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setType(type);
        permission.setOrderNum(orderNum);
        permission.setParent(parent);

        if (type == PermissionType.MENU)
            permission.setUrl(codeOrUrl);
        else
            permission.setCode(codeOrUrl);
        return permission;
    }

    private String getButtonName(String action) {
        return switch (action) {
            case "add" -> "新增";
            case "edit" -> "编辑";
            case "delete" -> "删除";
            case "search" -> "搜索";
            case "reset" -> "重置";
            default -> action;
        };
    }

    private Role createRole(String name, String description, Set<Permission> permissions) {
        Role role = new Role();
        role.setName(name);
        if (description != null)
            role.setDescription(description);
        role.setPermissions(permissions);
        return role;
    }

    private User createUser(String username, String realName, String password, Boolean superAdmin, String email, String phone, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setRealName(realName);
        user.setPassword(passwordEncoder.encode(password));
        user.setSuperAdmin(superAdmin);
        if (email != null)
            user.setEmail(email);
        if (phone != null)
            user.setPhone(phone);
        if (roles != null)
            user.setRoles(roles);
        return user;
    }
}
