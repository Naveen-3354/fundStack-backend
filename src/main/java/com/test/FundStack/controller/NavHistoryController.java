package com.test.FundStack.controller;


import com.test.FundStack.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> NavHistoryController
 *      - Initial Version.
 */

@RestController
@RequestMapping("/api/navhistory")
public class NavHistoryController extends CrudController<CrudController, Long>{
    
    protected NavHistoryController(CrudService<CrudController, Long> service) {
        super(service);
    }
    
    
}
