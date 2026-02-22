package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemeTransactions;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeTransactionRepo
 * - Initial Version.
 */

@Repository
public interface SchemeTransactionRepo extends CrudRepositoryBase<SchemeTransactions, Long>{
    
    Optional<SchemeTransactions > findBySchemeId(long schemeId);
}
