package com.test.FundStack.model.auth;

import com.test.FundStack.enums.RoleName;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String userId,
        String email,
        boolean active,
        int failedLoginAttempts,
        LocalDateTime suspendedUntil,
        boolean loginRevoked,
        Set<RoleName> roles
) {
}
