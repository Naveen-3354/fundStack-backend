package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -22-02-2026 <NaveenDhanasekaran> SystemTransactionRule
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class SystemTransactionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // SIP, STP, SWP

    @Enumerated(EnumType.STRING)
    private Frequency frequency; // DAILY, WEEKLY, MONTHLY, QUARTERLY, FORTNIGHTLY

    @ElementCollection
    @CollectionTable(name = "rule_days_or_dates", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "day_or_date")
    private List<String> dayOrDates; // E.g., ["Tuesday"], ["1","7","14"], ["01","02","03"]

    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    private Integer minInstallments;
    private Integer multiplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_transaction_id")
    @JsonBackReference
    private SchemeTransactions schemeTransaction;

    public enum TransactionType {
        SIP, STP, SWP
    }

    public enum Frequency {
        DAILY, WEEKLY, FORTNIGHTLY, MONTHLY, QUARTERLY
    }

    public static SystemTransactionRule.Frequency parseFrequency(String freqStr) {
        if (freqStr == null) return null;

        freqStr = freqStr.trim().toUpperCase();

        return switch (freqStr) {
            case "DAILY" -> Frequency.DAILY;
            case "WEEKLY" -> Frequency.WEEKLY;
            case "FORTNIGHTLY" -> Frequency.FORTNIGHTLY;
            case "MONTHLY" -> Frequency.MONTHLY;
            case "QUARTERLY" -> Frequency.QUARTERLY;
            default -> null;
        };
    }
}