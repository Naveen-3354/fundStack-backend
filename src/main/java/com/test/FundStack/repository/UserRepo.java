package com.test.FundStack.repository;

import com.test.FundStack.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gemini CLI
 */

@Repository
public interface UserRepo extends CrudRepositoryBase<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
