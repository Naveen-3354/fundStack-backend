package com.test.FundStack.repository;

import com.test.FundStack.entity.AppRole;
import com.test.FundStack.enums.RoleName;

import java.util.Optional;

public interface AppRoleRepository extends CrudRepositoryBase<AppRole, Long> {
    Optional<AppRole> findByName(RoleName name);
}
