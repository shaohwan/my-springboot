package com.demo.daniel.convert;

import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.PositionVO;
import com.demo.daniel.model.vo.RoleVO;
import com.demo.daniel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

public class UserConvert {

    private UserConvert() {
    }

    public static UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        Set<RoleVO> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(RoleConvert.convertToVO(role)));
        userVO.setRoles(roles);

        Set<PositionVO> positions = new HashSet<>();
        user.getPositions().forEach(position -> positions.add(PositionConvert.convertToVO(position)));
        userVO.setPositions(positions);
        return userVO;
    }

    public static User convertToEntity(UserUpsertDTO dto, User user, String... ignoreProperties) {
        User newUser = new User();
        if (user != null)
            newUser = user;
        BeanUtils.copyProperties(dto, newUser, ignoreProperties);
        return newUser;
    }
}
