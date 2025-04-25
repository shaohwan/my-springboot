package com.demo.daniel.repository;

import com.demo.daniel.model.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByRefreshTokenAndUserId(String refreshToken, Long userId);

    Optional<UserToken> findByUserId(Long userId);
}
