package com.test.FundStack.repository;

import com.test.FundStack.entity.NavHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -28-02-2026 <NaveenDhanasekaran> NavHistoryRepository
 * - Initial Version.
 */

@Repository
public interface NavHistoryRepository extends CrudRepositoryBase<NavHistory, Long> {

    boolean existsByAmfiCodeAndDate(String amfiCode, LocalDate date);

    @Query("SELECT n FROM NavHistory n WHERE n.amfiCode = :amfiCode AND n.navDate >= :fromDate ORDER BY n.navDate ASC")
    List<NavHistory> findNavsFromDate(
            @Param("amfiCode") String amfiCode,
            @Param("fromDate") LocalDate fromDate
    );

    Optional<NavHistory> findTopByAmfiCodeAndNavDateBeforeOrEqualOrderByNavDateDesc(String amfiCode, LocalDate date);
}
