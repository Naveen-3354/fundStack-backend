package com.test.FundStack.controller;


import com.test.FundStack.entity.SchemeDetails;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.SchemeDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeDetailsController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/api/scheme-details")
public class SchemeDetailsController extends CrudController<SchemeDetails, Long>{


    private final SchemeDetailsService schemeDetailsService;
    protected SchemeDetailsController(CrudService<SchemeDetails, Long> service,
                                      SchemeDetailsService schemeDetailsService) {
        super(service);
        this.schemeDetailsService = schemeDetailsService;
    }
    
    @GetMapping("/{schemeId}/overview/sysnc")
    public SchemeDetails syncOverview(@PathVariable("schemeId") long schemeId){
        return schemeDetailsService.syncSchemeDetails(schemeId);
    }

    @GetMapping("/{schemeId}/overview")
    public Optional<SchemeDetails> getOverViewSchemeDetails(
            @PathVariable("schemeId") long schemeId
    ) {
        return schemeDetailsService.getSchemeDetailsBySchemeId(schemeId);
    }
}
