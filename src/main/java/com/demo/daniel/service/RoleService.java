package com.demo.daniel.service;

import com.demo.daniel.model.dto.RoleCreateDTO;
import com.demo.daniel.model.dto.RoleUpdateDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.RoleDetailVO;
import com.demo.daniel.model.vo.RoleVO;
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

    public List<RoleVO> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
    }

    public RoleDetailVO getRoleDetail(Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    RoleDetailVO roleDetailVO = new RoleDetailVO();
                    BeanUtils.copyProperties(role, roleDetailVO);
                    Optional.ofNullable(role.getPermissions())
                            .ifPresent(permissions ->
                                    roleDetailVO.setPermissionIds(
                                            permissions.stream()
                                                    .map(Permission::getId)
                                                    .collect(Collectors.toList())
                                    )
                            );
                    return roleDetailVO;
                })
                .orElseThrow(() -> new RuntimeException("角色不存在"));
    }

    @Transactional
    public void createRole(RoleCreateDTO request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        if (request.getPermissionIds() != null) {
            List<Permission> permissions = permissionService.getPermissionByIds(request.getPermissionIds());
            role.setPermissions(new HashSet<>(permissions));
        } else {
            role.setPermissions(new HashSet<>());
        }
        roleRepository.save(role);
    }

    @Transactional
    public void updateRole(RoleUpdateDTO request) {
        Role role = roleRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getId()));
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        if (request.getPermissionIds() != null) {
            List<Permission> permissions = permissionService.getPermissionByIds(request.getPermissionIds());
            role.setPermissions(new HashSet<>(permissions));
        } else {
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
