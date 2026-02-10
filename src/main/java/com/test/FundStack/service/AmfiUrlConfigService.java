package com.test.FundStack.service;

import com.test.FundStack.repository.AmfiUrlConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmfiUrlConfigService {

    private final AmfiUrlConfigRepo amfiUrlConfigRepo;

    public String getUrlByKey(String key) {
        return amfiUrlConfigRepo.findByConfigKey(key)
                .map(config -> config.getConfigValue())
                .orElseThrow(() -> new IllegalStateException("AMFI URL config missing for key: " + key));
    }
}
