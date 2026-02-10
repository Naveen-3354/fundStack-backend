package com.test.FundStack.repository;

import com.test.FundStack.entity.AmfiUrlConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmfiUrlConfigRepo extends CrudRepositoryBase<AmfiUrlConfig, Long> {
    Optional<AmfiUrlConfig> findByConfigKey(String configKey);
}
