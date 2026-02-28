package com.test.FundStack.service;


import com.test.FundStack.entity.SchemeTransactions;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.SchemeTransactionRepo;
import org.springframework.stereotype.Service;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeTransactionService
 *      - Initial Version.
 */
 
@Service
public class SchemeTransactionService extends CrudService<SchemeTransactions, Long> {
    
    private final SchemeTransactionRepo schemeTransactionRepo;
    protected SchemeTransactionService(CrudRepositoryBase<SchemeTransactions, Long> repository,
                                       SchemeTransactionRepo schemeTransactionRepo) {
        super(repository);
        this.schemeTransactionRepo = schemeTransactionRepo;
    }

    public SchemeTransactions getSchemeTransactions(long schemeId) {
        return schemeTransactionRepo.findBySchemeId(schemeId)
                .orElse(null);
    }
}
