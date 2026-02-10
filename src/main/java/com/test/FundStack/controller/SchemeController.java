package com.test.FundStack.controller;


import com.test.FundStack.entity.AmfiSchemeDetails;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.model.amfi.SchemeList;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.SchemeDetailsService;
import com.test.FundStack.service.SchemeService;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> SchemeController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/scheme")
public class SchemeController extends CrudController<Scheme, Long> {
    
    private final SchemeService schemeService;
    private final SchemeDetailsService schemeDetailsService;
    
    protected SchemeController(CrudService<Scheme, Long> service,
                               SchemeService schemeService,
                               SchemeDetailsService schemeDetailsService) {
        super(service);
        this.schemeService = schemeService;
        this.schemeDetailsService = schemeDetailsService;
    }

    @GetMapping("/amfi/{amfiId}")
    public List<Scheme> getSchemeList(@PathVariable("amfiId") String amfiId) {
        return schemeService.getSchemesList(amfiId);
    }

    @GetMapping("/detail/{mfId}/{schemeId}")
    public AmfiSchemeDetails getSchemeDetails(
            @PathVariable("mfId") String mfId,
            @PathVariable("schemeId") String schemeId
            ) {
        return schemeDetailsService.saveSchemeDetails(null, mfId, schemeId);
    }

}
