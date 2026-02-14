package com.test.FundStack.model.schemeMigration;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> SchemeGroupedDto
 *      - Initial Version.
 */

@Getter
@AllArgsConstructor
public class SchemeGroupedDto {

    private String schemeName;
    private List<SchemeNavDto> navOptions;
}

