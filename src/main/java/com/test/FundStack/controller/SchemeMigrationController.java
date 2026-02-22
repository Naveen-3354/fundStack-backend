package com.test.FundStack.controller;


import com.test.FundStack.model.ApiResponse;
import com.test.FundStack.model.PaginationResponse;
import com.test.FundStack.model.schemeMigration.MigrationRequest;
import com.test.FundStack.model.schemeMigration.SchemeGroupedDto;
import com.test.FundStack.service.SchemeCsvService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> SchemeMigrationController
 *      - Initial Version.
 */

@Slf4j
@RestController
@RequestMapping("/api/admin/migration")
@RequiredArgsConstructor
public class SchemeMigrationController {
    
    private final SchemeCsvService schemeCsvService;

    @PostMapping("/scheme-name")
    public ResponseEntity<ApiResponse<String>> migrateBySchemeName(
            @Valid @RequestBody MigrationRequest request) {
        log.info("Migration triggered for schemeName={}", request.getValue());
        schemeCsvService.migrateBySchemeName(request.getValue());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Migration started for scheme name")
                        .data(request.getValue())
                        .build()
        );
    }
    
    @PostMapping("/amc-name")
    public ResponseEntity<ApiResponse<String>> migrateByAmcName(
            @Valid @RequestBody MigrationRequest request) {
        log.info("Migration triggered for amcName={}", request.getValue());
        schemeCsvService.migrateByAMCName(request.getValue());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Migration started for AMC name")
                        .data(request.getValue())
                        .build()
        );
    }

    @GetMapping("/grouped")
    public ResponseEntity<PaginationResponse<List<SchemeGroupedDto>>> getGrouped(
            @RequestParam(required = false) String amcName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                schemeCsvService.getGroupedSchemeCsv(amcName, page, size)
        );
    }


}
