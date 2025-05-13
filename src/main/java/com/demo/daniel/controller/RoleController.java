package com.demo.daniel.controller;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.RoleConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.RoleQueryDTO;
import com.demo.daniel.model.dto.RoleUpsertDTO;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.vo.RoleVO;
import com.demo.daniel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    // @PreAuthorize("hasAuthority('role:search')")
    public ApiResponse<Page<RoleVO>> getRoles(@ModelAttribute RoleQueryDTO request) {
        Page<RoleVO> roles = roleService.getRoles(request).map(RoleConvert::convertToVO);
        return ApiResponse.ok(roles);
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleVO> getRole(@PathVariable Long id) {
        Role role = roleService.getRole(id);
        return ApiResponse.ok(RoleConvert.convertToVO(role));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    @OperateLog(module = "角色管理", name = "新增角色", type = LogOperateType.ADD)
    public ApiResponse<Void> createRole(@RequestBody RoleUpsertDTO request) {
        roleService.upsertRole(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('role:edit')")
    @OperateLog(module = "角色管理", name = "编辑角色", type = LogOperateType.EDIT)
    public ApiResponse<Void> updateRole(@RequestBody RoleUpsertDTO request) {
        roleService.upsertRole(request);
        return ApiResponse.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('role:delete')")
    @OperateLog(module = "角色管理", name = "删除角色(们)", type = LogOperateType.DELETE)
    public ApiResponse<Void> deleteRoles(@RequestBody List<Long> ids) {
        roleService.deleteRoles(ids);
        return ApiResponse.ok();
    }
}
