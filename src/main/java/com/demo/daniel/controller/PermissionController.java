package com.demo.daniel.controller;

import com.demo.daniel.annotation.OperateLog;
import com.demo.daniel.convert.PermissionConvert;
import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.PermissionUpsertDTO;
import com.demo.daniel.model.entity.LogOperateType;
import com.demo.daniel.model.entity.Permission;
import com.demo.daniel.model.vo.PermissionVO;
import com.demo.daniel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    public ApiResponse<List<PermissionVO>> getMenuTree(@RequestParam(required = false) String name) {
        List<PermissionVO> menuTree = permissionService.getMenuTree(name).stream().map(PermissionConvert::convertToVO).collect(Collectors.toList());
        return ApiResponse.ok(menuTree);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('permission:add')")
    @OperateLog(module = "菜单管理", name = "新增菜单(按钮)", type = LogOperateType.ADD)
    public ApiResponse<Void> addPermission(@RequestBody PermissionUpsertDTO request) {
        permissionService.upsertPermission(request);
        return ApiResponse.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('permission:edit')")
    @OperateLog(module = "菜单管理", name = "编辑菜单(按钮)", type = LogOperateType.EDIT)
    public ApiResponse<Void> updatePermission(@RequestBody PermissionUpsertDTO request) {
        permissionService.upsertPermission(request);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionVO> getPermission(@PathVariable Long id) {
        Permission permission = permissionService.getPermission(id);
        return ApiResponse.ok(PermissionConvert.convertToVO(permission));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    @OperateLog(module = "菜单管理", name = "删除菜单(按钮)", type = LogOperateType.DELETE)
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.ok();
    }
}
