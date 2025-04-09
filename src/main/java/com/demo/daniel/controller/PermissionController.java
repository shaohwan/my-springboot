package com.demo.daniel.controller;

import com.demo.daniel.entity.Permission;
import com.demo.daniel.model.GenericResponse;
import com.demo.daniel.model.PermissionAddOrUpdateVO;
import com.demo.daniel.model.PermissionVO;
import com.demo.daniel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    public GenericResponse<List<Permission>> getMenuTree(@RequestParam(required = false) String name) {
        List<Permission> menuTree = permissionService.getMenuTreeByUsername(name);
        return GenericResponse.success(menuTree);
    }

    @PostMapping
    public Map<String, Object> addPermission(@RequestBody PermissionAddOrUpdateVO permissionAddOrUpdateVO) {
        permissionService.savePermission(permissionAddOrUpdateVO);
        return Map.of("success", true, "message", "添加成功");
    }

    @PutMapping
    public Map<String, Object> updatePermission(@RequestBody PermissionAddOrUpdateVO permissionAddOrUpdateVO) {
        permissionService.savePermission(permissionAddOrUpdateVO);
        return Map.of("success", true, "message", "修改成功");
    }

    @GetMapping("/{id}")
    public GenericResponse<PermissionVO> getPermissionById(@PathVariable Long id) {
        PermissionVO permission = permissionService.getPermissionById(id);
        return GenericResponse.success(permission);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<String> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return GenericResponse.success(null, "删除成功");
        } catch (IllegalStateException e) {
            return GenericResponse.error(400, e.getMessage());
        } catch (IllegalArgumentException e) {
            return GenericResponse.error(400, "权限不存在");
        } catch (Exception e) {
            return GenericResponse.error(500, "删除失败");
        }
    }
}
