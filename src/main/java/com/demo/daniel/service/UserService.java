package com.demo.daniel.service;

import com.demo.daniel.convert.UserConvert;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.UserQueryDTO;
import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import com.demo.daniel.util.UserSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

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

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(user -> userRepository.delete(user), () -> {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User ID " + id + " not found");
        });
    }

    public Set<String> getRoles(String username) {
        return userRepository.findByUsername(username).map(user -> user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getCode)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet()))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User Name " + username + " not found"));
    }
}
