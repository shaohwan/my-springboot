package com.demo.daniel.controller;

import com.demo.daniel.entity.Role;
import com.demo.daniel.model.GenericResponse;
import com.demo.daniel.model.RoleAddOrUpdateVO;
import com.demo.daniel.model.RoleVO;
import com.demo.daniel.service.PermissionService;
import com.demo.daniel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public GenericResponse<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return GenericResponse.success(roles, "Operation successful");
    }

    @GetMapping("/{id}")
    public GenericResponse<RoleVO> getRoleById(@PathVariable Long id) {
        RoleVO role = roleService.getRoleById(id);
        return GenericResponse.success(role, "Operation successful");
    }

    @PostMapping
    public Map<String, Object> saveRole(@RequestBody RoleAddOrUpdateVO roleAddOrUpdateVO) {
        roleService.saveRole(roleAddOrUpdateVO, roleAddOrUpdateVO.getPermissionIds());
        return Map.of("success", true, "message", "Role saved successfully");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<String> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return GenericResponse.success(null, "Role deleted successfully");
        } catch (IllegalStateException e) {
            return GenericResponse.error(400, e.getMessage());
        }
    }
}
