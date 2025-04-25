package com.demo.daniel.service;

import com.demo.daniel.model.dto.RefreshRequest;
import com.demo.daniel.model.entity.UserToken;
import com.demo.daniel.repository.TokenRepository;
import com.demo.daniel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService {

    private static final Long REFRESH_TOKEN_EXPIRY = 60 * 60 * 1000L;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    public Boolean isTokenExpired(RefreshRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> tokenRepository.findByRefreshTokenAndUserId(request.getRefreshToken(), user.getId())
                        .map(UserToken::getRefreshTokenExpiry)
                        .filter(expiry -> !expiry.isBefore(LocalDateTime.now()))
                        .isEmpty())
                .orElse(true);
    }

    public void saveToken(String username, String refreshToken, LocalDateTime refreshTokenExpiry) {
        userRepository.findByUsername(username).ifPresent(user -> {
            tokenRepository.findByUserId(user.getId()).ifPresentOrElse(
                    userToken -> {
                        // 更新 UserToken
                        userToken.setRefreshToken(refreshToken);
                        userToken.setRefreshTokenExpiry(refreshTokenExpiry);
                        tokenRepository.save(userToken);
                    },
                    () -> {
                        // 新增 UserToken
                        UserToken newUserToken = new UserToken();
                        newUserToken.setUserId(user.getId());
                        newUserToken.setRefreshToken(refreshToken);
                        newUserToken.setRefreshTokenExpiry(refreshTokenExpiry);
                        tokenRepository.save(newUserToken);
                    }
            );
        });
    }
}