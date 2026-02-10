package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemePlanOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionRepo
 * - Initial Version.
 */

@Repository
public interface SchemePlanOptionRepo extends CrudRepository<SchemePlanOption, Long> {
}
