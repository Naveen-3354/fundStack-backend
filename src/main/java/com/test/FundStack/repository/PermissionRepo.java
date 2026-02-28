package com.test.FundStack.repository;

import com.test.FundStack.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gemini CLI
 */

@Repository
public interface PermissionRepo extends CrudRepositoryBase<Permission, Long> {
    Optional<Permission> findByName(String name);
}
