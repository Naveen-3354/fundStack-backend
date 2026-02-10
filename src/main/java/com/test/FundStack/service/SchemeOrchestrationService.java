package com.test.FundStack.service;


import com.test.FundStack.entity.Scheme;
import com.test.FundStack.model.amfi.SchemeCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemeOrchestrationService
 *      - Initial Version.
 */

@Service
@RequiredArgsConstructor
public class SchemeOrchestrationService {

    private final SchemeDetailsService schemeDetailsService;
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSchemeCreated(Scheme scheme) {
        schemeDetailsService.saveSchemeDetails(
                scheme,
                scheme.getFundHouse().getAmfiId(),
                scheme.getAmfiId()
        );
    }
}
