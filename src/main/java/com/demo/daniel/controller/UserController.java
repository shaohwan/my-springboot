package com.demo.daniel.controller;

import com.demo.daniel.entity.User;
import com.demo.daniel.model.GenericResponse;
import com.demo.daniel.model.UserAddOrUpdateVO;
import com.demo.daniel.model.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public GenericResponse<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return GenericResponse.success(users, "Operation successful");
    }

    @GetMapping("/{id}")
    public GenericResponse<UserVO> getUser(@PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        return GenericResponse.success(user, "Operation successful");
    }

    @PostMapping
    public Map<String, Object> saveUser(@RequestBody UserAddOrUpdateVO userAddOrUpdateVO) {
        userService.saveUser(userAddOrUpdateVO);
        return Map.of("success", true, "message", "Role saved successfully");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return GenericResponse.success(null, "Role deleted successfully");
        } catch (Exception e) {
            return GenericResponse.error(400, e.getMessage());
        }
    }
}
