package com.test.FundStack.model.category;


import lombok.*;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -13-02-2026 <NaveenDhanasekaran> CategoryDTO
 *      - Initial Version.
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private Long parentId;
}
