package com.test.FundStack.model.amfi;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> SchemeList
 *      - Initial Version.
 */

@Getter
@Setter
public class SchemeList {

    @JsonProperty("scheme_id")
    private String schemeId;

    @JsonProperty("scheme_name")
    private String schemeName;
}

