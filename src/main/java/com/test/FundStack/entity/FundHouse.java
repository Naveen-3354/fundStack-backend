package com.test.FundStack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.FundStack.enums.FundType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FoundHouse
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "fund_house")
public class FundHouse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "amfi_id", nullable = false, unique = true)
    private String amfiId;

    @Column(name = "cams_kra_code")
    private String camsKraCode;

    @Column(name = "karvy_kra_code")
    private String karvyKraCode;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "fund_type", nullable = false)
    private FundType fundType;

    @OneToMany(
            mappedBy = "fundHouse",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Scheme> schemes = new ArrayList<>();
}
