package com.test.FundStack.repository;

import com.test.FundStack.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gemini CLI
 */

@Repository
public interface RoleRepo extends CrudRepositoryBase<Role, Long> {
    Optional<Role> findByName(String name);
}
