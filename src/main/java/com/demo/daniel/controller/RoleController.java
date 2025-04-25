package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.RoleCreateDTO;
import com.demo.daniel.model.dto.RoleQueryDTO;
import com.demo.daniel.model.dto.RoleUpdateDTO;
import com.demo.daniel.model.vo.RoleDetailVO;
import com.demo.daniel.model.vo.RoleVO;
import com.demo.daniel.service.PermissionService;
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

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    // @PreAuthorize("hasAuthority('role:search')")
    public ApiResponse<Page<RoleVO>> getAllRoles(@ModelAttribute RoleQueryDTO request) {
        Page<RoleVO> roles = roleService.getAllRoles(request);
        return ApiResponse.ok(roles);
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDetailVO> getRole(@PathVariable Long id) {
        RoleDetailVO role = roleService.getRoleDetail(id);
        return ApiResponse.ok(role);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    public ApiResponse<Void> createRole(@RequestBody RoleCreateDTO request) {
        roleService.createRole(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('role:edit')")
    public ApiResponse<Void> updateRole(@RequestBody RoleUpdateDTO request) {
        roleService.updateRole(request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.ok();
    }
}
