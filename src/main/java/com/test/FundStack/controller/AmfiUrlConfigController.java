package com.test.FundStack.controller;


import com.test.FundStack.entity.AmfiUrlConfig;
import com.test.FundStack.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author NaveenDhanasekaran
 * 
 * History:
 * -11-02-2026 <NaveenDhanasekaran> AmfiUrlConfigController
 *      - Initial Version.
 */
 
@RestController
@RequestMapping("/amfi-url")
public class AmfiUrlConfigController extends CrudController<AmfiUrlConfig, Long> {
    protected AmfiUrlConfigController(CrudService<AmfiUrlConfig, Long> service) {
        super(service);
    }
}
