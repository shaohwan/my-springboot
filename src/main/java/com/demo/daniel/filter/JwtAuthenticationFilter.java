package com.demo.daniel.filter;

import com.demo.daniel.exception.AuthException;
import com.demo.daniel.model.ErrorCode;
import com.demo.daniel.service.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Set<String> skipUrls = new HashSet<>(List.of("/api/auth/login", "/api/auth/refresh", "/api/auth/logout"));
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return skipUrls.stream().anyMatch(p -> new AntPathMatcher().match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        log.info("Get token from request: {}", token);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                var authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set for user: {}", username);
            }
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            log.error("Authentication failed: {}", e.getMessage());
            sendErrorResponse(response, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected authentication error: {}", e.getMessage(), e);
            sendErrorResponse(response, "Invalid token");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(401);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", ErrorCode.UNAUTHORIZED.getCode());
        errorResponse.put("message", message);
        errorResponse.put("timestamp", Instant.now().toString());

        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}
