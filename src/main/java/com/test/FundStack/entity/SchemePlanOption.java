package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> scheme_plan_option
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table
public class SchemePlanOption extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheme_nav_name")
    private String schemeNavName;

    @Column(name = "amfi_code")
    private String amfiCode;
    
    @Column(name = "option_type")
    private String optionType;

    @Column(name = "plan_type")
    private String planType;
    
    private String rtaSchemeCode;

    @Column(name = "isin_div_payout_growth")
    private String isinDivPayoutIsinGrowth;

    @Column(name = "isin_div_reinvestment")
    private String isinDivReinvestment;
    
    private String sebiCode;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheme_id", nullable = false)
    @JsonBackReference
    private Scheme scheme;
}
