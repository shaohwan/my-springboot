package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.dto.PermissionCreateDTO;
import com.demo.daniel.model.dto.PermissionUpdateDTO;
import com.demo.daniel.model.vo.PermissionDetailVO;
import com.demo.daniel.model.vo.PermissionVO;
import com.demo.daniel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ApiResponse.success(menuTree);
    }

    @PostMapping
    public ApiResponse<Void> addPermission(@RequestBody PermissionCreateDTO request) {
        permissionService.savePermission(request);
        return ApiResponse.success();
    }

    @PutMapping
    public ApiResponse<Void> updatePermission(@RequestBody PermissionUpdateDTO request) {
        permissionService.updatePermission(request);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionDetailVO> getPermissionById(@PathVariable Long id) {
        PermissionDetailVO permission = permissionService.getPermissionById(id);
        return ApiResponse.success(permission);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ApiResponse.success();
        } catch (IllegalStateException e) {
            return ApiResponse.failure(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("权限不存在");
        } catch (Exception e) {
            return ApiResponse.failure(500, "删除失败");
        }
    }
}
