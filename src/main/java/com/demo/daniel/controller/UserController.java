package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.UserCreateDTO;
import com.demo.daniel.model.dto.UserUpdateDTO;
import com.demo.daniel.model.vo.UserDetailVO;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<Page<UserVO>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserVO> users = userService.getAllUsers(PageRequest.of(page, size));
        return ApiResponse.ok(users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailVO> getUser(@PathVariable Long id) {
        UserDetailVO user = userService.getUserDetail(id);
        return ApiResponse.ok(user);
    }

    @PostMapping
    public ApiResponse<Void> createUser(@RequestBody UserCreateDTO request) {
        userService.createUser(request);
        return ApiResponse.ok();
    }

    @PutMapping
    public ApiResponse<Void> updateUser(@RequestBody UserUpdateDTO request) {
        userService.updateUser(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
