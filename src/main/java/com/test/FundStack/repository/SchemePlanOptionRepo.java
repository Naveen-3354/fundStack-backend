package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemePlanOption;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionRepo
 * - Initial Version.
 */

@Repository
public interface SchemePlanOptionRepo extends CrudRepositoryBase<SchemePlanOption, Long> {

    @Query("""
       select s.isinDivPayoutIsinGrowth
       from SchemePlanOption s
       where s.scheme.id = :schemeId
       """)
    Set<String> findExistingIsinsBySchemeId(@Param("schemeId") Long schemeId);

    @Query("""
       select s
       from SchemePlanOption s
       where s.scheme.id = :schemeId
       """)
    List<SchemePlanOption> findBySchemeId(@Param("schemeId") Long schemeId);
    
    Optional<SchemePlanOption> findByAmfiCode(String amfiCode);

}
