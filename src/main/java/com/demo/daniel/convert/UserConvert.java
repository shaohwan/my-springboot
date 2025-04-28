package com.demo.daniel.convert;

import com.demo.daniel.model.dto.UserUpsertDTO;
import com.demo.daniel.model.entity.User;
import com.demo.daniel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;

public class UserConvert {

    private UserConvert() {
    }

    public static UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
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
