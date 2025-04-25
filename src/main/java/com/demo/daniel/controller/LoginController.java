package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.LoginRequest;
import com.demo.daniel.model.dto.RefreshRequest;
import com.demo.daniel.model.vo.LoginVO;
import com.demo.daniel.model.vo.RefreshVO;
import com.demo.daniel.service.JwtTokenProvider;
import com.demo.daniel.service.LoginService;
import com.demo.daniel.service.TokenService;
import com.demo.daniel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@RequestBody LoginRequest request) {
        LoginVO loginVO = loginService.login(request);
        return ApiResponse.ok(loginVO);
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshVO> refreshToken(@RequestBody RefreshRequest request) {
        if (!tokenService.isTokenExpired(request)) {
            String accessToken = jwtTokenProvider.generateToken(request.getUsername(), userService.getRoles(request.getUsername()));
            RefreshVO refreshVO = new RefreshVO();
            refreshVO.setAccessToken(accessToken);
            refreshVO.setRefreshToken(request.getRefreshToken());
            return ApiResponse.ok(refreshVO);
        } else {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage());
        }
    }
}
