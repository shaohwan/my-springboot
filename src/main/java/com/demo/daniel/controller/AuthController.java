package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.LoginRequest;
import com.demo.daniel.model.dto.LogoutRequest;
import com.demo.daniel.model.dto.RefreshRequest;
import com.demo.daniel.model.vo.LoginVO;
import com.demo.daniel.model.vo.RefreshVO;
import com.demo.daniel.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@RequestBody LoginRequest request) {
        LoginVO loginVO = authService.login(request);
        return ApiResponse.ok(loginVO);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ApiResponse.ok();
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshVO> refreshToken(@RequestBody RefreshRequest request) {
        try {
            RefreshVO refreshVO = authService.refreshToken(request);
            return ApiResponse.ok(refreshVO);
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED.getCode(), e.getLocalizedMessage());
        }
    }
}
