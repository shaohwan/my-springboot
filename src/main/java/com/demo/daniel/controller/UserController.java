package com.demo.daniel.controller;

import com.demo.daniel.convert.UserConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.UserQueryDTO;
import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.User;
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
    public ApiResponse<Page<UserVO>> getUsers(@ModelAttribute UserQueryDTO request) {
        Page<UserVO> users = userService.getUsers(request).map(UserConvert::convertToVO);
        return ApiResponse.ok(users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserVO> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ApiResponse.ok(UserConvert.convertToVO(user));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public ApiResponse<Void> createUser(@RequestBody UserUpsertDTO request) {
        userService.upsertUser(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:edit')")
    public ApiResponse<Void> updateUser(@RequestBody UserUpsertDTO request) {
        userService.upsertUser(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok();
    }
}
