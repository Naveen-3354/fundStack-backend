package com.test.FundStack.repository;


import com.test.FundStack.entity.FundHouse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FundHouseRepo
 *      - Initial Version.
 */
 
@Repository
public interface FundHouseRepo extends CrudRepositoryBase<FundHouse, Long>{
    Optional<FundHouse> findByAmfiId(String amfiId);
}
