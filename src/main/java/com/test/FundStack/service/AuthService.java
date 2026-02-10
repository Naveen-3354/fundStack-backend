package com.test.FundStack.service;

import com.test.FundStack.entity.AppRole;
import com.test.FundStack.entity.AppUser;
import com.test.FundStack.model.auth.LoginRequest;
import com.test.FundStack.repository.AppUserRepository;
import com.test.FundStack.security.auth.JwtAuthenticationFilter;
import com.test.FundStack.security.auth.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${security.cookie.secure:true}")
    private boolean secureCookie;

    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {
        AppUser user = appUserRepository.findByUserId(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid credentials"));

        validateLoginState(user);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() > MAX_FAILED_ATTEMPTS) {
                user.setSuspendedUntil(LocalDateTime.now().plusHours(24));
            }
            appUserRepository.save(user);
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.setFailedLoginAttempts(0);
        appUserRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getUserId(),
                Map.of("roles", user.getRoles().stream().map(AppRole::getName).map(Enum::name).toList()));
        String refreshToken = jwtService.generateRefreshToken(user.getUserId());

        addCookie(response, JwtAuthenticationFilter.ACCESS_TOKEN_COOKIE, accessToken, (int) jwtService.getAccessTokenValiditySeconds());
        addCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, (int) jwtService.getRefreshTokenValiditySeconds());
    }

    @Transactional
    public void refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token missing");
        }

        Claims claims = jwtService.parse(refreshToken);
        Object tokenType = claims.get("type");
        if (tokenType == null || !"refresh".equals(tokenType.toString())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String userId = claims.getSubject();
        AppUser user = appUserRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validateLoginState(user);

        String accessToken = jwtService.generateAccessToken(user.getUserId(),
                Map.of("roles", user.getRoles().stream().map(AppRole::getName).map(Enum::name).toList()));

        addCookie(response, JwtAuthenticationFilter.ACCESS_TOKEN_COOKIE, accessToken, (int) jwtService.getAccessTokenValiditySeconds());
    }

    public void logout(HttpServletResponse response) {
        clearCookie(response, JwtAuthenticationFilter.ACCESS_TOKEN_COOKIE);
        clearCookie(response, REFRESH_TOKEN_COOKIE);
    }

    private void validateLoginState(AppUser user) {
        if (!user.isActive()) {
            throw new IllegalStateException("User is inactive");
        }
        if (user.isLoginRevoked()) {
            throw new IllegalStateException("User login is revoked by admin");
        }
        if (user.getSuspendedUntil() != null && user.getSuspendedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("User is suspended until " + user.getSuspendedUntil());
        }
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
