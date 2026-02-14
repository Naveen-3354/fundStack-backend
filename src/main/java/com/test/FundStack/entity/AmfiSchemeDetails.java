package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> AmfiSchemeDetails
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class AmfiSchemeDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mf_name", nullable = false)
    private String mfName;

    @Column(name = "scheme_name", nullable = false)
    private String schemeName;

    @Column(name = "scheme_type_desc")
    private String schemeTypeDesc;

    @Column(name = "scheme_cat_desc")
    private String schemeCatDesc;

    @Lob
    @Column(name = "scheme_objective")
    private String schemeObjective;

    @Lob
    @Column(name = "scheme_load")
    private String schemeLoad;

    @Column(name = "scheme_min_amt")
    private String schemeMinAmt;

    @Column(name = "launch_date")
    private OffsetDateTime launchDate;

    @Column(name = "amc_website")
    private String amcWebsite;

    @Column(name = "launch_risk")
    private String x;

    @Column(name = "current_risk")
    private String currentRisk;

    @Column(name = "sebi_category")
    private String sebiCategory;

    @Column(name = "risk_class")
    private String riskClass;
    
    @Lob
    @Column(name = "asset_allocation")
    private String assetAllocation;

    @Column(name = "face_value")
    private String faceValue;

    @Column(name = "nfo_open_date")
    private OffsetDateTime nfoOpenDate;

    @Column(name = "nfo_close_date")
    private OffsetDateTime nfoCloseDate;

    @Column(name = "allotment_date")
    private OffsetDateTime allotmentDate;

    @Column(name = "reopen_date")
    private OffsetDateTime reopenDate;

    @Column(name = "maturity_date")
    private String maturityDate;

    @Column(name = "benchmark_1")
    private String benchmark1;

    @Column(name = "benchmark_2")
    private String benchmark2;

    @Lob
    @Column(name = "exit_load")
    private String exitLoad;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "scheme_id",
            nullable = false,
            unique = true
    )
    @JsonBackReference
    private Scheme scheme;
}
