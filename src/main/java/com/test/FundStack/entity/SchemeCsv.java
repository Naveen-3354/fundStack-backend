package com.test.FundStack.entity;


import com.test.FundStack.enums.ImportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -13-02-2026 <NaveenDhanasekaran> SchemeCsv
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table
public class SchemeCsv extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String amcName;
    private String code;
    private String schemeName;
    private String schemeType;
    private String schemeCategory;
    private String schemeNavName;
    private String schemeMinAmt;
    private LocalDate launchDate;
    private LocalDate closureDate;
    private String isinDivPayOut;
    private String isinGrowthAndReinvest;
    private Integer version;
    private Boolean active;
    private LocalDate importDate;
    
    @Enumerated(EnumType.STRING)
    private ImportStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "scheme_id", nullable = true)
    private Scheme scheme;
    
}
