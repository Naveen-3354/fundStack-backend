package com.test.FundStack.controller;


import com.test.FundStack.entity.SchemeTransactions;
import com.test.FundStack.service.SchemeTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -22-02-2026 <NaveenDhanasekaran> SchemeTransactionController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/api/transaction-detial/{schemeId}")
@RequiredArgsConstructor
public class SchemeTransactionController {
    
    private final SchemeTransactionService schemeTransactionService;
    
    @GetMapping
    public SchemeTransactions getSchemeTransactions(@PathVariable long schemeId){
        return schemeTransactionService.getSchemeTransactions(schemeId);
    }
}
