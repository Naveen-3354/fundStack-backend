package com.test.FundStack.repository;

import com.test.FundStack.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepositoryBase<AppUser, Long> {
    Optional<AppUser> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}
