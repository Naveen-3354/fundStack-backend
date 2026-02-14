package com.test.FundStack.model.schemeMigration;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> SchemeNavDto
 *      - Initial Version.
 */

@Getter
@AllArgsConstructor
public class SchemeNavDto {

    private String schemeNavName;
    private String code;
    private String isinDivPayOut;
    private String isinGrowthAndReinvest;
    private LocalDate launchDate;
    private boolean migrated;
}
