package com.test.FundStack.service;


import com.test.FundStack.entity.NavHistory;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.entity.SchemeReturns;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.NavHistoryRepository;
import com.test.FundStack.repository.SchemeReturnsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> SchemeReturnsService
 *      - Initial Version.
 */
 
@Service
public class SchemeReturnsService extends CrudService<SchemeReturns, Long> {

    private final NavHistoryRepository navRepository;
    private final SchemeReturnsRepository schemeReturnsRepository;
    
    protected SchemeReturnsService(CrudRepositoryBase<SchemeReturns, Long> repository,
                                   NavHistoryRepository navRepository,
                                   SchemeReturnsRepository schemeReturnsRepository) {
        super(repository);
        this.navRepository = navRepository;
        this.schemeReturnsRepository = schemeReturnsRepository;
    }

    public SchemeReturns calculateReturns(String amfiCode, LocalDate lunchDate, LocalDate asOnDate) {
        SchemeReturns sr = new SchemeReturns();
        sr.setAmfiCode(amfiCode);
        sr.setAsOnDate(asOnDate);
        BigDecimal navToday = getClosestNav(amfiCode, asOnDate);
        sr.setReturn1d(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusDays(1))));
        sr.setReturn1m(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusMonths(1))));
        sr.setReturn3m(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusMonths(3))));
        sr.setReturn6m(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusMonths(6))));
        sr.setReturn1y(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusYears(1))));
        sr.setReturn3y(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusYears(3))));
        sr.setReturn5y(calcReturn(navToday, getClosestNav(amfiCode, asOnDate.minusYears(5))));
        sr.setCagr3y(calcCAGR(getClosestNav(amfiCode, asOnDate.minusYears(3)), navToday, 3));
        sr.setCagr5y(calcCAGR(getClosestNav(amfiCode, asOnDate.minusYears(5)), navToday, 5));
        sr.setCagrSinceInception(calcCAGR(getClosestNav(amfiCode, lunchDate), navToday,
                (double) ChronoUnit.DAYS.between(lunchDate, asOnDate) / 365.25));
        return schemeReturnsRepository.save(sr);
    }

    private BigDecimal getClosestNav(String amfiCode, LocalDate date) {
        return navRepository.findTopByAmfiCodeAndNavDateBeforeOrEqualOrderByNavDateDesc(amfiCode, date)
                .map(NavHistory::getValue)
                .orElse(null);
    }

    private BigDecimal calcReturn(BigDecimal navEnd, BigDecimal navStart) {
        if (navEnd == null || navStart == null) return null;
        return navEnd.subtract(navStart)
                .divide(navStart, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calcCAGR(BigDecimal navStart, BigDecimal navEnd, double years) {
        if (navStart == null || navEnd == null || years <= 0) return null;
        BigDecimal ratio = navEnd.divide(navStart, 6, RoundingMode.HALF_UP);
        double cagr = Math.pow(ratio.doubleValue(), 1 / years) - 1;
        return BigDecimal.valueOf(cagr * 100).setScale(4, RoundingMode.HALF_UP);
    }
    
    public SchemeReturns getSchemeReturn(String amfiCode){
        return schemeReturnsRepository.findByAmfiCode(amfiCode)
                .orElse(null);
    }
    
}
