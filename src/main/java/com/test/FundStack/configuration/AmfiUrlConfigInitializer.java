package com.test.FundStack.configuration;

import com.test.FundStack.entity.AmfiUrlConfig;
import com.test.FundStack.repository.AmfiUrlConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AmfiUrlConfigInitializer implements CommandLineRunner {

    private final AmfiProperties amfiProperties;
    private final AmfiUrlConfigRepo amfiUrlConfigRepo;

    @Override
    @Transactional
    public void run(String... args) {
        amfiProperties.asMap().forEach((key, value) -> {
            if (value == null || value.isBlank()) {
                return;
            }

            amfiUrlConfigRepo.findByConfigKey(key)
                    .orElseGet(() -> amfiUrlConfigRepo.save(
                            AmfiUrlConfig.builder()
                                    .configKey(key)
                                    .configValue(value)
                                    .build()
                    ));
        });
    }
}
