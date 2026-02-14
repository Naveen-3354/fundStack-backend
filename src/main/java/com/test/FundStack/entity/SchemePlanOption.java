package com.test.FundStack.entity;


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

    @Column(name = "repurchase_price")
    private String repurchasePrice;

    @Column(name = "sales_price")
    private String salesPrice;

    @Column(name = "average_aum_for_quarter", precision = 19, scale = 2)
    private BigDecimal averageAumForTheQuarter;

    @Column(name = "aum_as_at")
    private String asAtTheEndOf;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;
}
