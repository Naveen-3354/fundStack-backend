package com.test.FundStack.controller;


import com.test.FundStack.service.AmfiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> AmfiController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/amfi")
@RequiredArgsConstructor
public class AmfiController {

    private final AmfiService amfiService;
    
    @GetMapping("/getschemeCsv")
    public ResponseEntity<Void> getSchemeCsv() throws Exception {
        amfiService.fetchAndImportCsv();
        return ResponseEntity.ok().build();
    }
}
