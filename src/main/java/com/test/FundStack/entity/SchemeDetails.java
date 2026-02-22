package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDate;

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
public class SchemeDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String mfName;
    
    private String schemeName;
    
    private String schemeMinAmt;

    @Column(columnDefinition = "TEXT")
    private String schemeObjective;
    
    private String schemeCatDesc;
    
    @Column(columnDefinition = "TEXT")
    private String entryLoad;
    
    @Column(name = "scheme_type")
    private String schemeType;

    @Column(name = "amc_website")
    private String amcWebsite;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Column(name = "allotment_date")
    private LocalDate allotmentDate;

    private String annualExpenseDir;

    private String annualExpenseReg;

    private String auditor;

    @Column(name = "benchmark_1")
    private String benchmark1;

    @Column(name = "benchmark_2")
    private String benchmark2;

    private String custodian;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String exitLoad;

    @Column(name = "face_value")
    private String faceValue;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "nfo_open_date")
    private LocalDate nfoOpenDate;

    @Column(name = "nfo_close_date")
    private LocalDate nfoCloseDate;
    
    private String potentialRiskoMeter;

    private String registrar;

    @Column(name = "reopen_date")
    private LocalDate reopenDate;
    
    private String riskoMeterAsOnDate;
    
    private String riskoMeterAtLaunch;
    
    private String sidePocketing;

    @Column(columnDefinition = "TEXT")
    private String assetAllocation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "scheme_id",
            nullable = false,
            unique = true
    )
    @JsonBackReference
    private Scheme scheme;
}
