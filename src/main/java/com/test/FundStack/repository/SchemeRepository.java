package com.test.FundStack.repository;


import com.test.FundStack.entity.FundHouse;
import com.test.FundStack.entity.Scheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    Optional<Scheme> findByName(String name);

    @Query("""
    SELECT s FROM Scheme s
    WHERE (:fundHouseIds IS NULL OR s.fundHouse.id IN :fundHouseIds)
      AND LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    Page<Scheme> searchSchemes(
            @Param("fundHouseIds") List<Long> fundHouseIds,
            @Param("name") String name,
            Pageable pageable
    );

    Optional<Scheme> findByNameAndFundHouse(String schemeName, FundHouse fundHouse);
}
