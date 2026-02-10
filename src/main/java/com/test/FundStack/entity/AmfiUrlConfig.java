package com.test.FundStack.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Stores AMFI endpoint URLs in DB so runtime services can resolve URLs from table instead of static config.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "amfi_url_config")
public class AmfiUrlConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", nullable = false, unique = true)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;
}
