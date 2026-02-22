package com.test.FundStack.service;


import com.test.FundStack.entity.SchemeManager;
import com.test.FundStack.repository.CrudRepositoryBase;
import org.springframework.stereotype.Service;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeManagerService
 *      - Initial Version.
 */
 
@Service
public class SchemeManagerService extends CrudService<SchemeManager, Long> {
    protected SchemeManagerService(CrudRepositoryBase<SchemeManager, Long> repository) {
        super(repository);
    }
}
