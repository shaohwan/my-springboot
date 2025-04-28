package com.demo.daniel.controller;

import com.demo.daniel.convert.RoleConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.RoleQueryDTO;
import com.demo.daniel.model.dto.RoleUpsertDTO;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.vo.RoleVO;
import com.demo.daniel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<Void> createRole(@RequestBody RoleUpsertDTO request) {
        roleService.upsertRole(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('role:edit')")
    public ApiResponse<Void> updateRole(@RequestBody RoleUpsertDTO request) {
        roleService.upsertRole(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.ok();
    }
}
