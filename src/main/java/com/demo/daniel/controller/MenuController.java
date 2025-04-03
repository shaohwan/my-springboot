package com.demo.daniel.controller;

import com.demo.daniel.model.GenericResponse;
import com.demo.daniel.model.MenuVO;
import com.demo.daniel.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/tree")
    public GenericResponse<List<MenuVO>> getMenuTree() {
        List<MenuVO> menuTree = menuService.getMenuTree();
        return GenericResponse.success(menuTree);
    }
}
