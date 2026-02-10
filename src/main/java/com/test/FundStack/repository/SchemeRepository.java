package com.test.FundStack.repository;


import com.test.FundStack.entity.Scheme;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> SchemeRepository
 *      - Initial Version.
 */
 
@Repository
public interface SchemeRepository extends CrudRepositoryBase<Scheme, Long>{
    List<Scheme> findByFundHouseId(Long id);
}
