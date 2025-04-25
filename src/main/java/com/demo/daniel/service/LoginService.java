package com.demo.daniel.service;

import com.demo.daniel.model.dto.LoginRequest;
import com.demo.daniel.model.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoginService {

    private static final LocalDateTime REFRESH_TOKEN_EXPIRY = LocalDateTime.now().plusDays(1);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    public LoginVO login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

        String username = request.getUsername();
        String accessToken = jwtTokenProvider.generateToken(username, userService.getRoles(username));
        String refreshToken = UUID.randomUUID().toString();

        LoginVO loginVO = new LoginVO();
        loginVO.setUsername(username);
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);

        tokenService.saveToken(username, refreshToken, REFRESH_TOKEN_EXPIRY);
        return loginVO;
    }
}
