package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> SchemeManager
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class SchemeManager extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;
    
    @ManyToOne
    @JoinColumn(name = "fund_manager_id")
    @JsonBackReference
    private FundManager fundManager;
    
    private String role;
    
    private LocalDate fromDate;
    
    private LocalDate toDate;
}
