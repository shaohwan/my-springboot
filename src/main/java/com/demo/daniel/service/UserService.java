package com.demo.daniel.service;

import com.demo.daniel.convert.UserConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.UserQueryDTO;
import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.repository.PermissionRepository;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import com.demo.daniel.util.UserSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User ID " + id + " not found"));
    }

    public Page<User> getUsers(UserQueryDTO request) {
        Specification<User> spec = UserSpecifications.buildSpecification(request.getUsername(), request.getEmail());
        return userRepository.findAll(spec, PageRequest.of(request.getPage(), request.getSize()));
    }

    @Transactional
    public void upsertUser(UserUpsertDTO request) {
        User user = Optional.ofNullable(request.getId())
                .flatMap(id -> userRepository.findById(id))
                .map(s -> UserConvert.convertToEntity(request, s, "password"))
                .orElseGet(() -> {
                    User u = UserConvert.convertToEntity(request, null);
                    u.setPassword(passwordEncoder.encode(request.getPassword()));
                    return u;
                });

        user.setRoles(Optional.ofNullable(request.getRoleIds())
                .map(roleIds -> new HashSet<>(roleRepository.findAllById(roleIds)))
                .orElseGet(HashSet::new));

        userRepository.save(user);
    }

    public void deleteUsers(List<Long> ids) {
        List<String> errors = new ArrayList<>();
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        ids.forEach(id -> userRepository.findById(id).ifPresentOrElse(
                user -> {
                    if (currentUsername.equals(user.getUsername()))
                        errors.add("User " + user.getUsername() + " (ID: " + id + ") cannot be deleted as it is the current user");
                    else
                        userRepository.deleteById(id);
                },
                () -> errors.add("User ID " + id + " not found")
        ));

        Optional.of(errors)
                .filter(errs -> !errs.isEmpty())
                .ifPresent(errs -> {
                    throw new BusinessException(ErrorCode.USER_IS_LOGIN.getCode(),
                            "User(s) cannot be deleted: " + String.join("; ", errs));
                });
    }

    public Set<String> getRoles(String username) {
        return userRepository.findByUsername(username).map(user -> {
            Stream<Permission> sp = user.getSuperAdmin() ? permissionRepository.findAll().stream()
                    : user.getRoles().stream().flatMap(role -> role.getPermissions().stream());
            return sp.map(Permission::getCode).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        }).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode()
                , "User Name " + username + " not found"));
    }
}
