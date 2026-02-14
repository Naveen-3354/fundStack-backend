package com.test.FundStack.repository;

import com.test.FundStack.entity.SchemeCsv;
import com.test.FundStack.model.schemeMigration.SchemeGroupProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -13-02-2026 <NaveenDhanasekaran> SchemeCsvRepository
 * - Initial Version.
 */

@Repository
public interface SchemeCsvRepository extends CrudRepository<SchemeCsv, Long> {
    boolean existsByCodeAndImportDate(String code, LocalDate importDate);
    List<SchemeCsv> findBySchemeName(String name);

    List<SchemeCsv> findByAmcName(String name);

    @Query("""
                SELECT DISTINCT s.schemeName
                FROM SchemeCsv s
                WHERE (:amcName IS NULL OR s.amcName = :amcName)
            """)
    Page<String> findDistinctSchemeNamesByAmc(
            @Param("amcName") String amcName,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT
        s.amcName AS amcName,
        s.schemeName AS schemeName,
        s.schemeCategory AS schemeCategory
    FROM SchemeCsv s
    WHERE (:amcName IS NULL OR s.amcName = :amcName)
""")
    Page<SchemeGroupProjection> findDistinctGroups(
            @Param("amcName") String amcName,
            Pageable pageable
    );

    List<SchemeCsv> findByAmcNameAndSchemeNameInAndSchemeCategoryIn(
            String amcName,
            List<String> schemeNames,
            List<String> categories
    );

    List<SchemeCsv> findBySchemeNameIn(List<String> schemeNames);
}
