package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemeManager;
import org.springframework.stereotype.Repository;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeFundManagerRepo
 * - Initial Version.
 */

@Repository
public interface SchemeManagerRepo extends CrudRepositoryBase<SchemeManager, Long> {
}
