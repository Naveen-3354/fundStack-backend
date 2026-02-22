package com.test.FundStack.controller;


import com.test.FundStack.entity.FundHouse;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.SchemeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FundHouseController
 *      - Initial Version.
 */
 
@RestController
@RequestMapping("/api/fund-house")
public class FundHouseController extends CrudController<FundHouse, Long>{

    private final SchemeService schemeService;
    protected FundHouseController(CrudService<FundHouse, Long> service,
                                  SchemeService schemeService) {
        super(service);
        this.schemeService = schemeService;
    }
    
    
    @GetMapping("/amfi/{amfiId}")
    public List<Scheme> getSchemeList(@PathVariable("amfiId") String amfiId) {
        return schemeService.getSchemesList(amfiId);
    }
}
