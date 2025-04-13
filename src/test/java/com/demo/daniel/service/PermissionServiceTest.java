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

import java.util.Set;

@SpringBootTest
public class PermissionServiceTest {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddPermission() {
        Permission root = new Permission();
        root.setName("权限管理");
        root.setType(PermissionType.MENU);
        root.setPath("/home");
        permissionRepository.save(root);

        Permission userMenu = new Permission();
        userMenu.setName("用户管理");
        userMenu.setType(PermissionType.MENU);
        userMenu.setPath("/user");
        userMenu.setComponent("components/User");
        userMenu.setParent(root);
        permissionRepository.save(userMenu);

        Permission userAddBtn = new Permission();
        userAddBtn.setName("新增");
        userAddBtn.setCode("user:add");
        userAddBtn.setType(PermissionType.BUTTON);
        userAddBtn.setParent(userMenu);
        permissionRepository.save(userAddBtn);

        Permission userEditBtn = new Permission();
        userEditBtn.setName("编辑");
        userEditBtn.setCode("user:edit");
        userEditBtn.setType(PermissionType.BUTTON);
        userEditBtn.setParent(userMenu);
        permissionRepository.save(userEditBtn);

        Permission userDeleteBtn = new Permission();
        userDeleteBtn.setName("删除");
        userDeleteBtn.setCode("user:delete");
        userDeleteBtn.setType(PermissionType.BUTTON);
        userDeleteBtn.setParent(userMenu);
        permissionRepository.save(userDeleteBtn);

        Permission roleMenu = new Permission();
        roleMenu.setName("角色管理");
        roleMenu.setType(PermissionType.MENU);
        roleMenu.setPath("/role");
        roleMenu.setComponent("components/Role");
        roleMenu.setParent(root);
        permissionRepository.save(roleMenu);

        Permission roleAddBtn = new Permission();
        roleAddBtn.setName("新增");
        roleAddBtn.setCode("role:add");
        roleAddBtn.setType(PermissionType.BUTTON);
        roleAddBtn.setParent(roleMenu);
        permissionRepository.save(roleAddBtn);

        Permission roleEditBtn = new Permission();
        roleEditBtn.setName("编辑");
        roleEditBtn.setCode("role:edit");
        roleEditBtn.setType(PermissionType.BUTTON);
        roleEditBtn.setParent(roleMenu);
        permissionRepository.save(roleEditBtn);

        Permission roleDeleteBtn = new Permission();
        roleDeleteBtn.setName("删除");
        roleDeleteBtn.setCode("role:delete");
        roleDeleteBtn.setType(PermissionType.BUTTON);
        roleDeleteBtn.setParent(roleMenu);
        permissionRepository.save(roleDeleteBtn);

        Permission permissionMenu = new Permission();
        permissionMenu.setName("菜单管理");
        permissionMenu.setType(PermissionType.MENU);
        permissionMenu.setPath("/permission");
        permissionMenu.setComponent("components/Permission");
        permissionMenu.setParent(root);
        permissionRepository.save(permissionMenu);

        Permission permissionAddBtn = new Permission();
        permissionAddBtn.setName("新增");
        permissionAddBtn.setCode("permission:add");
        permissionAddBtn.setType(PermissionType.BUTTON);
        permissionAddBtn.setParent(permissionMenu);
        permissionRepository.save(permissionAddBtn);

        Permission permissionEditBtn = new Permission();
        permissionEditBtn.setName("编辑");
        permissionEditBtn.setCode("permission:edit");
        permissionEditBtn.setType(PermissionType.BUTTON);
        permissionEditBtn.setParent(permissionMenu);
        permissionRepository.save(permissionEditBtn);

        Permission permissionDeleteBtn = new Permission();
        permissionDeleteBtn.setName("删除");
        permissionDeleteBtn.setCode("permission:delete");
        permissionDeleteBtn.setType(PermissionType.BUTTON);
        permissionDeleteBtn.setParent(permissionMenu);
        permissionRepository.save(permissionDeleteBtn);

        // 初始化角色
        Role adminRole = new Role();
        adminRole.setName("管理员");
        adminRole.setPermissions(Set.of(root, userMenu, userAddBtn, userEditBtn, userDeleteBtn, roleMenu, roleAddBtn,
                roleEditBtn, roleDeleteBtn, permissionMenu, permissionAddBtn, permissionEditBtn, permissionDeleteBtn));
        roleRepository.save(adminRole);

        User user = new User();
        user.setRealName("daniel");
        user.setUsername("daniel");
        user.setPassword("123456");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
    }
}
