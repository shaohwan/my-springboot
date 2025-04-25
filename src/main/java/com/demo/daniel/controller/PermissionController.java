package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.PermissionCreateDTO;
import com.demo.daniel.model.dto.PermissionUpdateDTO;
import com.demo.daniel.model.vo.PermissionDetailVO;
import com.demo.daniel.model.vo.PermissionVO;
import com.demo.daniel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    public ApiResponse<List<PermissionVO>> getMenuTree(@RequestParam(required = false) String name) {
        List<PermissionVO> menuTree = permissionService.getMenuTreeByUsername(name);
        return ApiResponse.ok(menuTree);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('permission:add')")
    public ApiResponse<Void> addPermission(@RequestBody PermissionCreateDTO request) {
        permissionService.savePermission(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('permission:edit')")
    public ApiResponse<Void> updatePermission(@RequestBody PermissionUpdateDTO request) {
        permissionService.updatePermission(request);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionDetailVO> getPermissionById(@PathVariable Long id) {
        PermissionDetailVO permission = permissionService.getPermissionById(id);
        return ApiResponse.ok(permission);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.ok();
    }
}
