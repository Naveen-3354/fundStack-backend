package com.test.FundStack.service;


import com.test.FundStack.entity.FundHouse;
import com.test.FundStack.repository.CrudRepositoryBase;
import org.springframework.stereotype.Service;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FundHouseService
 *      - Initial Version.
 */
 
@Service
public class FundHouseService extends CrudService<FundHouse, Long>{
    
    protected FundHouseService(CrudRepositoryBase<FundHouse, Long> repository) {
        super(repository);
    }
    
}
