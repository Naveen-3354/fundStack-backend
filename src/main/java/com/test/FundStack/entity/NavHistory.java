package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> NavHistory
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class NavHistory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String amfiCode;

    private LocalDate date;

    @Column(precision = 12, scale = 4)
    private BigDecimal value;
    
}
