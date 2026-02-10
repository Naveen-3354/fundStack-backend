package com.test.FundStack.controller;


import com.test.FundStack.entity.FundHouse;
import com.test.FundStack.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FundHouseController
 *      - Initial Version.
 */
 
@RestController
@RequestMapping("/fund-house")
public class FundHouseController extends CrudController<FundHouse, Long>{
    
    
    protected FundHouseController(CrudService<FundHouse, Long> service) {
        super(service);
    }
}
