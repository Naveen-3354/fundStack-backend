package com.test.FundStack.controller;


import com.test.FundStack.entity.SchemeDetails;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.model.PaginationResponse;
import com.test.FundStack.model.amfi.SchemeList;
import com.test.FundStack.service.*;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> SchemeController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/api/scheme")
public class SchemeController extends CrudController<Scheme, Long> {
    
    private final SchemeService schemeService;
    
    protected SchemeController(CrudService<Scheme, Long> service,
                               SchemeService schemeService) {
        super(service);
        this.schemeService = schemeService;
    }
    
    @GetMapping("/page")
    public ResponseEntity<PaginationResponse<List<Scheme>>> getAll(
            @RequestParam(defaultValue = "1")int pageNo, @RequestParam(defaultValue = "10")int pageSize,
            @RequestParam(required = false) List<Long> fundHouseIds, @RequestParam(required = false)String search
    ) {
        PaginationResponse<List<Scheme>> response =
                schemeService.getSchemes(pageNo, pageSize, fundHouseIds, search);
        return ResponseEntity.ok(response);
    }

}
