package com.demo.daniel.service;

import com.demo.daniel.entity.Menu;
import com.demo.daniel.model.MenuVO;
import com.demo.daniel.repository.MenuRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<MenuVO> getMenuTree() {
        List<Menu> menus = menuRepository.findAllByOrderBySortAsc();
        return buildMenuTree(menus, 0L);
    }

    private List<MenuVO> buildMenuTree(List<Menu> menus, Long parentId) {
        List<MenuVO> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                MenuVO vo = new MenuVO();
                BeanUtils.copyProperties(menu, vo);
                vo.setChildren(buildMenuTree(menus, menu.getId()));
                result.add(vo);
            }
        }
        return result;
    }
}
