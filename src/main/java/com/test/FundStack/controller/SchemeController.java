package com.test.FundStack.controller;


import com.test.FundStack.entity.AmfiSchemeDetails;
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
@RequestMapping("/scheme")
public class SchemeController extends CrudController<Scheme, Long> {
    
    private final SchemeService schemeService;
    private final SchemeDetailsService schemeDetailsService;
    private final SchemePlanOptionService schemePlanOptionService;
    
    private final AmfiService amfiService;
    
    protected SchemeController(CrudService<Scheme, Long> service,
                               SchemeService schemeService,
                               AmfiService amfiService,
                               SchemeDetailsService schemeDetailsService,
                               SchemePlanOptionService schemePlanOptionService) {
        super(service);
        this.schemeService = schemeService;
        this.schemeDetailsService = schemeDetailsService;
        this.schemePlanOptionService = schemePlanOptionService;
        this.amfiService = amfiService;
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
    
    @GetMapping("/{schemeId}/overview/sysnc")
    public AmfiSchemeDetails syncOverview(@PathVariable("schemeId") long schemeId){
        return schemeDetailsService.syncSchemeDetails(schemeId);
    }

    @GetMapping("/{schemeId}/overview")
    public Optional<AmfiSchemeDetails> getOverViewSchemeDetails(
            @PathVariable("schemeId") long schemeId
    ) {
        return schemeDetailsService.getSchemeDetailsBySchemeId(schemeId);
    }

    @GetMapping("/{schemeId}/options")
    public List<SchemePlanOption> getNavOverViewSchemeDetails(
            @PathVariable("schemeId") long schemeId
    ) {
        return schemePlanOptionService.getAllNavDetailsByScheme(schemeId);
    }

    @GetMapping("/{schemeId}/navOverview/load")
    public ResponseEntity<Void> loadNavOverViewSchemeDetails(
            @PathVariable("schemeId") long schemeId
    ) {
        schemePlanOptionService.saveAllNavDetails(schemeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/docs/xml/{schemeId}")
    public ResponseEntity<?> extractDataFromXml(
            @PathVariable("schemeId") String schemeId
    ) {
        return ResponseEntity.ok().body(schemePlanOptionService.getXmlData(schemeId));
    }
    
    @GetMapping("/getschemeCsv")
    public ResponseEntity<Void> getSchemeCsv() throws Exception {
        amfiService.fetchAndImportCsv();
        return ResponseEntity.ok().build();
    }

}
