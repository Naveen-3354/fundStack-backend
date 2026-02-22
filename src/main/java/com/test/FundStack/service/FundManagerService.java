package com.test.FundStack.service;


import com.test.FundStack.entity.FundManager;
import com.test.FundStack.repository.CrudRepositoryBase;
import org.springframework.stereotype.Service;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> FundManagerService
 *      - Initial Version.
 */
 
@Service
public class FundManagerService extends CrudService<FundManager, Long> {
    protected FundManagerService(CrudRepositoryBase<FundManager, Long> repository) {
        super(repository);
    }
}
