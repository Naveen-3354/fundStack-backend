package com.test.FundStack.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> SchemeReturns
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "scheme_returns",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"scheme_id", "as_on_date"})}
)
@ToString
public class SchemeReturns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String amfiCode;

    @Column(name = "as_on_date", nullable = false)
    private LocalDate asOnDate;

    @Column(name = "return_1d", precision = 8, scale = 4)
    private BigDecimal return1d;

    @Column(name = "return_1m", precision = 8, scale = 4)
    private BigDecimal return1m;

    @Column(name = "return_3m", precision = 8, scale = 4)
    private BigDecimal return3m;

    @Column(name = "return_6m", precision = 8, scale = 4)
    private BigDecimal return6m;

    @Column(name = "return_1y", precision = 8, scale = 4)
    private BigDecimal return1y;

    @Column(name = "return_3y", precision = 8, scale = 4)
    private BigDecimal return3y;

    @Column(name = "return_5y", precision = 8, scale = 4)
    private BigDecimal return5y;

    @Column(name = "cagr_3y", precision = 8, scale = 4)
    private BigDecimal cagr3y;

    @Column(name = "cagr_5y", precision = 8, scale = 4)
    private BigDecimal cagr5y;

    @Column(name = "cagr_since_inception", precision = 8, scale = 4)
    private BigDecimal cagrSinceInception;
}
