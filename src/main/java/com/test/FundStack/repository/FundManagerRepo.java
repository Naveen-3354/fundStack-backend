package com.test.FundStack.repository;


import com.test.FundStack.entity.FundManager;
import org.springframework.stereotype.Repository;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> FundManagerRepo
 *      - Initial Version.
 */
 
@Repository
public interface FundManagerRepo extends CrudRepositoryBase<FundManager, Long> {
}
