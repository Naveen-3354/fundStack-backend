package com.test.FundStack.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -20-02-2026 <NaveenDhanasekaran> FundManager
 *      - Initial Version.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class FundManager extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @OneToMany(mappedBy = "fundManager")
    @JsonManagedReference
    private List<SchemeManager> schemeManagers = new ArrayList<>();
}
