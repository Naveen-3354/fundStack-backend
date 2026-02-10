package com.test.FundStack.controller;


import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/scheme-plan")
public class SchemePlanOptionController extends CrudController<SchemePlanOption , Long>{
    protected SchemePlanOptionController(CrudService<SchemePlanOption, Long> service) {
        super(service);
    }
}
