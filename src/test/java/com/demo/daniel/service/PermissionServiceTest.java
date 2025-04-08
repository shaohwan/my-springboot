package com.demo.daniel.service;

import com.demo.daniel.entity.Permission;
import com.demo.daniel.entity.PermissionType;
import com.demo.daniel.entity.Role;
import com.demo.daniel.entity.User;
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
        Permission permissionMenu = new Permission();
        permissionMenu.setName("权限管理");
        permissionMenu.setType(PermissionType.MENU);
        permissionMenu.setPath("/home");
        permissionMenu.setComponent("components/Home.vue");
        permissionMenu.setIcon("p");
        permissionRepository.save(permissionMenu);

        Permission userMenu = new Permission();
        userMenu.setName("用户管理");
        userMenu.setType(PermissionType.MENU);
        userMenu.setPath("/person");
        userMenu.setComponent("components/Person.vue");
        userMenu.setIcon("user");
        userMenu.setParent(permissionMenu);
        permissionRepository.save(userMenu);

        // 初始化按钮权限
        Permission searchBtn = new Permission();
        searchBtn.setName("查询");
        searchBtn.setCode("user:search");
        searchBtn.setType(PermissionType.BUTTON);
        searchBtn.setParent(userMenu);
        permissionRepository.save(searchBtn);

        Permission resetBtn = new Permission();
        resetBtn.setName("重置");
        resetBtn.setCode("user:reset");
        resetBtn.setType(PermissionType.BUTTON);
        resetBtn.setParent(userMenu);
        permissionRepository.save(resetBtn);

        // 初始化角色
        Role adminRole = new Role();
        adminRole.setName("管理员");
        adminRole.setPermissions(Set.of(permissionMenu, userMenu, searchBtn, resetBtn));
        roleRepository.save(adminRole);

        User user = new User();
        user.setRealName("daniel");
        user.setUsername("daniel");
        user.setPassword("123456");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
    }
}
