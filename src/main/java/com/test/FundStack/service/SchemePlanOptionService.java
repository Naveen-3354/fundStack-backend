package com.test.FundStack.service;


import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.repository.CrudRepositoryBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionSerivce
 *      - Initial Version.
 */
 
@Slf4j
@Service
public class SchemePlanOptionService extends CrudService<SchemePlanOption, Long> {
    protected SchemePlanOptionService(CrudRepositoryBase<SchemePlanOption, Long> repository) {
        super(repository);
    }
}
