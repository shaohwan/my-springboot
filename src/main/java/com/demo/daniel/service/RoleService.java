package com.demo.daniel.service;

import com.demo.daniel.entity.Permission;
import com.demo.daniel.entity.Role;
import com.demo.daniel.entity.User;
import com.demo.daniel.model.RoleAddOrUpdateVO;
import com.demo.daniel.model.RoleVO;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRepository userRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public RoleVO getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(role, roleVO);
                    Optional.ofNullable(role.getPermissions())
                            .ifPresent(permissions ->
                                    roleVO.setPermissionIds(
                                            permissions.stream()
                                                    .map(Permission::getId)
                                                    .collect(Collectors.toList())
                                    )
                            );
                    return roleVO;
                })
                .orElseThrow(() -> new RuntimeException("角色不存在"));
    }

    @Transactional
    public void saveRole(RoleAddOrUpdateVO roleAddOrUpdateVO, List<Long> permissionIds) {
        Role role;
        if (roleAddOrUpdateVO.getId() == null) {
            // 新增角色
            role = new Role();
        } else {
            // 编辑角色
            role = roleRepository.findById(roleAddOrUpdateVO.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleAddOrUpdateVO.getId()));
        }
        role.setName(roleAddOrUpdateVO.getName());
        role.setDescription(roleAddOrUpdateVO.getDescription());

        // 处理权限
        if (permissionIds != null) {
            List<Permission> permissions = permissionService.getPermissionByIds(permissionIds);
            role.setPermissions(new HashSet<>(permissions));
        } else {
            // 如果 permissionIds 为 null，清空权限
            role.setPermissions(new HashSet<>());
        }

        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<User> usersWithRole = userRepository.findByRolesContaining(role);

        if (!usersWithRole.isEmpty()) {
            throw new IllegalStateException("无法删除角色 " + role.getName() + " 因为它已被 " + usersWithRole.size() + " 个用户使用!");
        }
        roleRepository.deleteById(id);
    }
}
