package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemeReturns;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -28-02-2026 <NaveenDhanasekaran> SchemeReturnsRepository
 * - Initial Version.
 */

@Repository
public interface SchemeReturnsRepository extends CrudRepositoryBase<SchemeReturns, Long>{
    
    Optional<SchemeReturns> findByAmfiCode(String amfiCode);
}
