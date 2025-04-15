package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.vo.UserVO;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ApiResponse<UserVO> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        UserVO userVO = userService.login(name, password);
        return ApiResponse.ok(userVO);
    }
}
