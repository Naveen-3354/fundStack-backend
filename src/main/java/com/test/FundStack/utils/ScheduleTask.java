package com.test.FundStack.utils;


import com.test.FundStack.service.AmfiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> ScheduleTask
 *      - Initial Version.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleTask {

    private final AmfiService amfiImportService;

    @Scheduled(cron = "0 0 */3 * * *")
    public void importAmfiData() {

        log.info("AMFI import started at {}", LocalDateTime.now());

        try {
            amfiImportService.fetchAndImportCsv();
        } catch (Exception e) {
            log.error("Error during AMFI import", e);
        }

        log.info("AMFI import completed at {}", LocalDateTime.now());
    }
}
