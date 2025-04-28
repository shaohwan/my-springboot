package com.demo.daniel.service;

import com.demo.daniel.convert.RoleConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.RoleQueryDTO;
import com.demo.daniel.model.dto.RoleUpsertDTO;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import com.demo.daniel.util.RoleSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserRepository userRepository;

    public Role getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_EXIST.getCode(), "Role ID " + id + " not found"));
    }

    public Page<Role> getRoles(RoleQueryDTO request) {
        Specification<Role> spec = RoleSpecifications.buildSpecification(request.getName());
        return roleRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize()));
    }

    @Transactional
    public void upsertRole(RoleUpsertDTO request) {
        Role role = Optional.ofNullable(request.getId())
                .flatMap(id -> roleRepository.findById(id))
                .map(r -> RoleConvert.convertToEntity(request, r))
                .orElseGet(() -> RoleConvert.convertToEntity(request, null));

        role.setPermissions(Optional.ofNullable(request.getPermissionIds())
                .map(permissionIds -> new HashSet<>(permissionRepository.findAllById(permissionIds)))
                .orElseGet(HashSet::new));

        roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        roleRepository.findById(id).ifPresentOrElse(role -> userRepository.findByRolesContaining(role)
                .filter(users -> !users.isEmpty()).ifPresentOrElse(users -> {
                    throw new BusinessException(ErrorCode.ROLE_IN_USE.getCode(),
                            "Role " + role.getName() + " used by " + users.size() + " users");
                }, () -> roleRepository.deleteById(id)), () -> {
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST.getCode(),
                    "Role ID " + id + " not found");
        });
    }
}
