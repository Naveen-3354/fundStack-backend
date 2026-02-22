package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> Scheme
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "scheme")
public class Scheme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "amfi_id", nullable = false, unique = true)
    private String amfiId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_house_id", nullable = false)
    @JsonBackReference
    private FundHouse fundHouse;
    
    private boolean planOptionsSynced = false;
    private boolean basicDetailsSynced = false;
    private boolean xmlDetailsSynced = false;
    private boolean transactionDetailsSynced = false;

    @OneToOne(
            mappedBy = "scheme",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private SchemeDetails amfiDetails;

    @OneToOne(
            mappedBy = "scheme",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private SchemeTransactions schemeTransactions;

    @OneToMany(mappedBy = "scheme",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SchemeManager> schemeManagers = new ArrayList<>();

    @OneToMany(
            mappedBy = "scheme",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @JsonManagedReference
    private List<SchemePlanOption> planOptions = new ArrayList<>();
}
