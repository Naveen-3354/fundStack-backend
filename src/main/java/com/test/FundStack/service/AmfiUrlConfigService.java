package com.test.FundStack.service;

import com.test.FundStack.entity.AmfiUrlConfig;
import com.test.FundStack.repository.AmfiUrlConfigRepo;
import com.test.FundStack.repository.CrudRepositoryBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AmfiUrlConfigService extends CrudService<AmfiUrlConfig, Long>{

    private final AmfiUrlConfigRepo amfiUrlConfigRepo;

    protected AmfiUrlConfigService(CrudRepositoryBase<AmfiUrlConfig, Long> repository,
                                   AmfiUrlConfigRepo amfiUrlConfigRepo) {
        super(repository);
        this.amfiUrlConfigRepo = amfiUrlConfigRepo;
    }

    public String getUrlByKey(String key) {
        return amfiUrlConfigRepo.findByConfigKey(key)
                .map(config -> config.getConfigValue())
                .orElseThrow(() -> new IllegalStateException("AMFI URL config missing for key: " + key));
    }
}
