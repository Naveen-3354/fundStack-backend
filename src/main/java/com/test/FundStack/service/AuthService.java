package com.test.FundStack.service;

import com.test.FundStack.entity.Role;
import com.test.FundStack.entity.User;
import com.test.FundStack.model.auth.AuthenticationRequest;
import com.test.FundStack.model.auth.AuthenticationResponse;
import com.test.FundStack.model.auth.RegisterRequest;
import com.test.FundStack.repository.RoleRepo;
import com.test.FundStack.repository.UserRepo;
import com.test.FundStack.model.auth.RefreshTokenRequest;
import com.test.FundStack.security.JwtService;
import com.test.FundStack.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Gemini CLI
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
        } else {
            // Default role if none provided
            Role userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(Role.builder().name("USER").build()));
            roles.add(userRole);
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .enabled(true)
                .build();
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(com.test.FundStack.entity.RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return AuthenticationResponse.builder()
                            .token(token)
                            .refreshToken(request.getRefreshToken())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
