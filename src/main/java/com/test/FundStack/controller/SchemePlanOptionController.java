package com.test.FundStack.controller;


import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.SchemePlanOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/api/scheme-plan")
public class SchemePlanOptionController extends CrudController<SchemePlanOption , Long>{
    
    private final SchemePlanOptionService schemePlanOptionService;
    protected SchemePlanOptionController(CrudService<SchemePlanOption, Long> service,
                                         SchemePlanOptionService schemePlanOptionService) {
        super(service);
        this.schemePlanOptionService = schemePlanOptionService;
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
}
