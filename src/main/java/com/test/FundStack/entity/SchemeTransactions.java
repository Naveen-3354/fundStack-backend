package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeTransactions
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class SchemeTransactions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic transaction limits
    private BigDecimal maxRedAmt;
    @Enumerated(EnumType.STRING)
    private Unit maxRedUnit;

    private BigDecimal minRedAmt;
    @Enumerated(EnumType.STRING)
    private Unit minRedUnit;

    private BigDecimal minBalAmt;
    @Enumerated(EnumType.STRING)
    private Unit minBalUnit;

    private BigDecimal minSwtAmt;
    @Enumerated(EnumType.STRING)
    private Unit minSwtUnit;

    private BigDecimal minimumAddAmount;
    private Integer minimumAddAmountInMul;

    private BigDecimal minimumAppAmount;
    private Integer minimumAppAmountInMul;

    private BigDecimal swgPrice;
    private BigDecimal swtMulAmt;
    @Enumerated(EnumType.STRING)
    private Unit swtMulUnit;

    // System rules (SIP/STP/SWP)
    @OneToMany(mappedBy = "schemeTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SystemTransactionRule> systemTransactionRules = new ArrayList<>();

    // Link to Scheme
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheme_id", nullable = false, unique = true)
    @JsonBackReference
    private Scheme scheme;

    public enum Unit {
        NA, AMOUNT, UNITS
    }

    public List<SystemTransactionRule> getSystemTransactionRules() {
        if (systemTransactionRules == null) {
            systemTransactionRules = new ArrayList<>();
        }
        return systemTransactionRules;
    }

}