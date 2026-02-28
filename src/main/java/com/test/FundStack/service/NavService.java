package com.test.FundStack.service;


import com.test.FundStack.entity.NavHistory;
import com.test.FundStack.enums.FundType;
import com.test.FundStack.model.navHistory.NavHistoryResponse;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.NavHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> NavService
 *      - Initial Version.
 */
 
@Slf4j
@Service
public class NavService extends CrudService<NavHistory, Long> {
    
    private final RestTemplate restTemplate;
    private final NavHistoryRepository navHistoryRepository;
    private final AmfiService amfiService;
    
    protected NavService(CrudRepositoryBase<NavHistory, Long> repository,
                         NavHistoryRepository navHistoryRepository,
                         RestTemplate restTemplate,
                         AmfiService amfiService) {
        super(repository);
        this.restTemplate = restTemplate;
        this.navHistoryRepository = navHistoryRepository;
        this.amfiService = amfiService;
    }

    public void loadHistoricNav(FundType fundType, String amfiCode, LocalDate allotmentDate) {
        amfiService.getNavHistory(fundType, amfiCode, allotmentDate).forEach(response -> {
            if(response.getData() == null){
                log.info("No records found for: {}", amfiCode);
            }else {
                response.getData().getNavGroups().forEach(navGroup -> {
                    navGroup.getHistoricalRecords().forEach(record -> {
                        LocalDate date = LocalDate.parse(record.getDate());
                        if (!navHistoryRepository.existsByAmfiCodeAndDate(amfiCode, date)) {
                            if (record.getNav() != null && !record.getNav().isBlank()) {
                                navHistoryRepository.save(NavHistory.builder()
                                        .amfiCode(amfiCode)
                                        .date(date)
                                        .value(new BigDecimal(record.getNav()))
                                        .build());
                            }
                        }
                    });
                });
            }
        });
    }

    public List<NavHistory> getNavByMonths(String amfiCode, int months) {
        LocalDate fromDate = LocalDate.now().minusMonths(months);
        List<NavHistory> rawNavs = navHistoryRepository.findNavsFromDate(amfiCode, fromDate);
        return aggregateNavs(rawNavs, months);
    }

    private List<NavHistory> aggregateNavs(List<NavHistory> rawNavs, int months) {
        if (months <= 3) return rawNavs;
        Map<YearMonth, List<NavHistory>> monthMap = rawNavs.stream()
                .collect(Collectors.groupingBy(n -> YearMonth.from(n.getDate())));
        List<NavHistory> aggregated = new ArrayList<>();
        for (List<NavHistory> monthRecords : monthMap.values()) {

            if (months <= 6) {
                aggregated.add(monthRecords.get(0));
                if (monthRecords.size() > 1)
                    aggregated.add(monthRecords.get(monthRecords.size() / 2));
            } else {
                aggregated.add(monthRecords.get(0));
            }
        }

        aggregated.sort(Comparator.comparing(NavHistory::getDate));

        return aggregated;
    }
}
