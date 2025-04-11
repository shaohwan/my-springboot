package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.UserCreateDTO;
import com.demo.daniel.model.dto.UserUpdateDTO;
import com.demo.daniel.model.vo.UserDetailVO;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return ApiResponse.success(users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailVO> getUser(@PathVariable Long id) {
        UserDetailVO user = userService.getUserDetail(id);
        return ApiResponse.success(user);
    }

    @PostMapping
    public ApiResponse<Void> createUser(@RequestBody UserCreateDTO request) {
        userService.createUser(request);
        return ApiResponse.success();
    }

    @PutMapping
    public ApiResponse<Void> updateUser(@RequestBody UserUpdateDTO request) {
        userService.updateUser(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
}
