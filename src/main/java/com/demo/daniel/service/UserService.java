package com.demo.daniel.service;

import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.UserCreateDTO;
import com.demo.daniel.model.dto.UserUpdateDTO;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.UserDetailVO;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<UserVO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }

    public UserDetailVO getUserDetail(Long id) {
        return userRepository.findById(id).map(user -> {
                    UserDetailVO userDetailVO = new UserDetailVO();
                    BeanUtils.copyProperties(user, userDetailVO);
                    return userDetailVO;
                })
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "用户不存在: " + id));
    }

    @Transactional
    public void createUser(UserCreateDTO request) {
        User user = new User();
        user.setPassword(request.getPassword());

        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setEnabled(request.getEnabled());

        if (request.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateDTO request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "用户不存在: " + request.getId()));

        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setEnabled(request.getEnabled());

        if (request.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "用户不存在: " + id));
        userRepository.delete(user);
    }

    public UserVO login(String name, String password) {
        return userRepository.findByUsernameAndPassword(name, password).map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS.getCode(), ErrorCode.INVALID_CREDENTIALS.getMessage()));
    }
}
