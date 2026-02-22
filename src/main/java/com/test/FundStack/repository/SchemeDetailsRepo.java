package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemeDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -09-02-2026 <NaveenDhanasekaran> AmfiSchemeDetailsRepo
 * - Initial Version.
 */

@Repository
public interface SchemeDetailsRepo extends CrudRepositoryBase<SchemeDetails, Long>{
    Optional<SchemeDetails> findBySchemeId(Long schemeId);
}
