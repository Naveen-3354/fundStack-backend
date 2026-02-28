package com.test.FundStack.repository;

import com.test.FundStack.entity.RefreshToken;
import com.test.FundStack.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Gemini CLI
 */

@Repository
public interface RefreshTokenRepo extends CrudRepositoryBase<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
