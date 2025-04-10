package com.demo.daniel.service;

import com.demo.daniel.entity.Role;
import com.demo.daniel.entity.User;
import com.demo.daniel.model.UserAddOrUpdateVO;
import com.demo.daniel.model.UserVO;
import com.demo.daniel.repository.RoleRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserVO getUserById(Long id) {
        return userRepository.findById(id).map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return userVO;
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void saveUser(UserAddOrUpdateVO userAddOrUpdateVO) {
        User user;
        if (userAddOrUpdateVO.getId() == null) {
            user = new User();
            user.setPassword(userAddOrUpdateVO.getPassword()); // 假设密码已加密
        } else {
            user = userRepository.findById(userAddOrUpdateVO.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userAddOrUpdateVO.getId()));
        }
        user.setUsername(userAddOrUpdateVO.getUsername());
        user.setRealName(userAddOrUpdateVO.getRealName());
        user.setEmail(userAddOrUpdateVO.getEmail());
        user.setPhone(userAddOrUpdateVO.getPhone());
        user.setEnabled(userAddOrUpdateVO.getEnabled());

        if (userAddOrUpdateVO.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userAddOrUpdateVO.getRoleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
