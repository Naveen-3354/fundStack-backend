package com.test.FundStack.model.auth;

import com.test.FundStack.enums.RoleName;

import java.util.Set;

public record UserUpsertRequest(
        String userId,
        String email,
        String password,
        Boolean active,
        Set<RoleName> roles
) {
}
