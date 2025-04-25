package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.UserCreateDTO;
import com.demo.daniel.model.dto.UserQueryDTO;
import com.demo.daniel.model.dto.UserUpdateDTO;
import com.demo.daniel.model.vo.UserDetailVO;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    // @PreAuthorize("hasAuthority('user:search')")
    public ApiResponse<Page<UserVO>> getAllUsers(@ModelAttribute UserQueryDTO request) {
        Page<UserVO> users = userService.getAllUsers(request);
        return ApiResponse.ok(users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailVO> getUser(@PathVariable Long id) {
        UserDetailVO user = userService.getUserDetail(id);
        return ApiResponse.ok(user);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public ApiResponse<Void> createUser(@RequestBody UserCreateDTO request) {
        userService.createUser(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:edit')")
    public ApiResponse<Void> updateUser(@RequestBody UserUpdateDTO request) {
        userService.updateUser(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
