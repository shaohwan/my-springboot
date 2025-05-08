package com.demo.daniel.service;

import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.PermissionType;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class PermissionServiceTest {

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

    @Test
    public void testAddPermission() {
        // Create root permission
        Permission root = createPermission("权限管理", PermissionType.MENU, null, 0, null);
        permissionRepository.save(root);

        // Create menu permissions and their buttons
        Set<Permission> allPermissions = new HashSet<>();
        allPermissions.add(root);

        for (int i = 0; i < MENU_NAMES.length; i++) {
            // Create menu permission
            Permission menu = createPermission(
                    MENU_NAMES[i],
                    PermissionType.MENU,
                    MENU_URLS[i],
                    i,
                    root
            );
            permissionRepository.save(menu);
            allPermissions.add(menu);

            // Create button permissions for each menu
            for (int j = 0; j < BUTTON_ACTIONS.length; j++) {
                Permission button = createPermission(
                        getButtonName(BUTTON_ACTIONS[j]),
                        PermissionType.BUTTON,
                        MENU_CODES[i] + ":" + BUTTON_ACTIONS[j],
                        j,
                        menu
                );
                permissionRepository.save(button);
                allPermissions.add(button);
            }
        }

        // Initialize admin role with all permissions
        Role adminRole = new Role();
        adminRole.setName("管理员");
        adminRole.setDescription("管理员");
        adminRole.setPermissions(allPermissions);
        roleRepository.save(adminRole);

        // Initialize admin user
        User adminUser = createUser(
                "daniel",
                "Daniel Wang",
                "123456",
                Boolean.FALSE,
                "daniel@qq.com",
                "13913133777",
                Set.of(adminRole)
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

        if (type == PermissionType.MENU) {
            permission.setUrl(codeOrUrl);
        } else {
            permission.setCode(codeOrUrl);
        }

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
