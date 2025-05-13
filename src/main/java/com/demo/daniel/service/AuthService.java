package com.demo.daniel.service;

import com.demo.daniel.exception.AuthException;
import com.demo.daniel.exception.BusinessException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.model.dto.LoginRequest;
import com.demo.daniel.model.dto.LogoutRequest;
import com.demo.daniel.model.dto.RefreshRequest;
import com.demo.daniel.model.entity.LogLoginOperation;
import com.demo.daniel.model.entity.LogStatus;
import com.demo.daniel.model.entity.UserToken;
import com.demo.daniel.model.vo.LoginVO;
import com.demo.daniel.model.vo.RefreshVO;
import com.demo.daniel.repository.TokenRepository;
import com.demo.daniel.repository.UserRepository;
import com.demo.daniel.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private LogLoginService logLoginService;

    public LoginVO login(LoginRequest request) {
        userRepository.findByUsername(request.getUsername()).filter(user -> !user.getEnabled()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.USER_DISABLED.getCode(), ErrorCode.USER_DISABLED.getMessage());
        });

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS.getCode(), ErrorCode.INVALID_CREDENTIALS.getMessage());
        }

        String username = request.getUsername();
        String accessToken = jwtTokenProvider.generateToken(username, userService.getRoles(username));
        String refreshToken = UUID.randomUUID().toString();

        LoginVO loginVO = new LoginVO();
        loginVO.setUsername(username);
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);

        LocalDateTime refreshTokenExpiry = LocalDateTime.now().plusDays(request.getRememberMe()
                ? AppConstants.REFRESH_EXPIRY_REMEMBER : AppConstants.REFRESH_EXPIRY_NOT_REMEMBER);

        upsertToken(username, refreshToken, refreshTokenExpiry);
        return loginVO;
    }

    public void logout(LogoutRequest request) {
        String username = request.getUsername();
        userRepository.findByUsername(username).ifPresentOrElse(user -> tokenRepository.findByUserId(user.getId()).ifPresent(ut -> {
            ut.setRefreshTokenExpiry(LocalDateTime.now());
            tokenRepository.save(ut);

            logLoginService.saveLog(username, LogStatus.SUCCESS, LogLoginOperation.LOGOUT_SUCCESS);
        }), () -> {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User Name " + username + " not found");
        });
    }

    public RefreshVO refreshToken(RefreshRequest request) {
        if (!isTokenExpired(request)) {
            String accessToken = jwtTokenProvider.generateToken(request.getUsername(), userService.getRoles(request.getUsername()));
            RefreshVO refreshVO = new RefreshVO();
            refreshVO.setAccessToken(accessToken);
            refreshVO.setRefreshToken(request.getRefreshToken());
            return refreshVO;
        } else {
            throw new AuthException(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage());
        }
    }

    public Boolean isTokenExpired(RefreshRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> tokenRepository.findByRefreshTokenAndUserId(request.getRefreshToken(), user.getId())
                        .map(UserToken::getRefreshTokenExpiry)
                        .filter(expiry -> !expiry.isBefore(LocalDateTime.now()))
                        .isEmpty())
                .orElse(true);
    }

    public void upsertToken(String username, String refreshToken, LocalDateTime refreshTokenExpiry) {
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            UserToken userToken = new UserToken();
            tokenRepository.findByUserId(user.getId()).ifPresent(ut -> userToken.setId(ut.getId()));
            userToken.setUserId(user.getId());
            userToken.setRefreshToken(refreshToken);
            userToken.setRefreshTokenExpiry(refreshTokenExpiry);
            tokenRepository.save(userToken);
        }, () -> {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getCode(), "User Name " + username + " not found");
        });
    }
}
