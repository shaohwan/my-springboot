package com.demo.daniel.controller;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.UserConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.UpdatePasswordDTO;
import com.demo.daniel.model.dto.UserQueryDTO;
import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @OperateLog(module = "用户管理", name = "新增用户", type = LogOperateType.ADD)
    public ApiResponse<Void> createUser(@RequestBody UserUpsertDTO request) {
        userService.upsertUser(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:edit')")
    @OperateLog(module = "用户管理", name = "编辑用户", type = LogOperateType.EDIT)
    public ApiResponse<Void> updateUser(@RequestBody UserUpsertDTO request) {
        userService.upsertUser(request);
        return ApiResponse.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('user:delete')")
    @OperateLog(module = "用户管理", name = "删除用户(们)", type = LogOperateType.DELETE)
    public ApiResponse<Void> deleteUsers(@RequestBody List<Long> ids) {
        userService.deleteUsers(ids);
        return ApiResponse.ok();
    }

    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestBody UpdatePasswordDTO request) {
        userService.updatePassword(request);
        return ApiResponse.ok();
    }
}
