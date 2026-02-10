package com.test.FundStack.service;

import com.test.FundStack.entity.AppRole;
import com.test.FundStack.entity.AppUser;
import com.test.FundStack.enums.RoleName;
import com.test.FundStack.model.auth.UserResponse;
import com.test.FundStack.model.auth.UserUpsertRequest;
import com.test.FundStack.repository.AppRoleRepository;
import com.test.FundStack.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CredentialNotificationService credentialNotificationService;

    @Transactional
    public UserResponse create(UserUpsertRequest request) {
        if (appUserRepository.existsByUserId(request.userId())) {
            throw new IllegalStateException("userId already exists");
        }
        if (appUserRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("email already exists");
        }

        AppUser user = AppUser.builder()
                .userId(request.userId())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .active(request.active() == null || request.active())
                .failedLoginAttempts(0)
                .loginRevoked(false)
                .roles(resolveRoles(request.roles()))
                .build();

        AppUser saved = appUserRepository.save(user);
        credentialNotificationService.sendCredentials(saved.getEmail(), request.userId(), request.password());
        return map(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserUpsertRequest request) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.active() != null) {
            user.setActive(request.active());
        }
        if (request.roles() != null && !request.roles().isEmpty()) {
            user.setRoles(resolveRoles(request.roles()));
        }

        return map(appUserRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return map(appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Transactional(readOnly = true)
    public java.util.List<UserResponse> getAll() {
        return appUserRepository.findAll().stream().map(this::map).toList();
    }

    @Transactional
    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }

    @Transactional
    public UserResponse revokeLogin(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setLoginRevoked(true);
        return map(appUserRepository.save(user));
    }

    @Transactional
    public UserResponse suspendForHours(Long id, long hours) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setSuspendedUntil(LocalDateTime.now().plusHours(hours));
        return map(appUserRepository.save(user));
    }

    @Transactional
    public UserResponse unsuspendAndUnrevoke(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setLoginRevoked(false);
        user.setSuspendedUntil(null);
        user.setFailedLoginAttempts(0);
        return map(appUserRepository.save(user));
    }

    private Set<AppRole> resolveRoles(Set<RoleName> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new IllegalArgumentException("At least one role is required");
        }

        return roleNames.stream().map(name -> appRoleRepository.findByName(name)
                        .orElseThrow(() -> new IllegalStateException("Role missing in DB: " + name)))
                .collect(Collectors.toSet());
    }

    private UserResponse map(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.isActive(),
                user.getFailedLoginAttempts(),
                user.getSuspendedUntil(),
                user.isLoginRevoked(),
                user.getRoles().stream().map(AppRole::getName).collect(Collectors.toSet())
        );
    }
}
