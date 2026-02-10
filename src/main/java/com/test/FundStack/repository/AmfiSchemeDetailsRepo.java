package com.test.FundStack.repository;

import com.test.FundStack.entity.AmfiSchemeDetails;
import com.test.FundStack.entity.Scheme;
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
public interface AmfiSchemeDetailsRepo extends CrudRepositoryBase<AmfiSchemeDetails, Long>{
    Optional<AmfiSchemeDetails> findByScheme(Scheme scheme);
}
