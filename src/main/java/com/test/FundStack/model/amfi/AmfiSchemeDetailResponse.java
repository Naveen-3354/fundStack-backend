package com.test.FundStack.model.amfi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> AmfiSchemeDetailResponse
 *      - Initial Version.
 */

@Data
public class AmfiSchemeDetailResponse {

    @JsonProperty("MF_Name")
    private String mfName;

    @JsonProperty("Scheme_Name")
    private String schemeName;

    @JsonProperty("Scheme_Objective")
    private String schemeObjective;

    @JsonProperty("SchemeType_Desc")
    private String schemeTypeDesc;

    @JsonProperty("SchemeCat_Desc")
    private String schemeCatDesc;

    @JsonProperty("Scheme_load")
    private String schemeLoad;

    @JsonProperty("Scheme_min_amt")
    private String schemeMinAmt;

    @JsonProperty("Launch_Date")
    private OffsetDateTime launchDate;

    @JsonProperty("AMC_Website")
    private String amcWebsite;

    @JsonProperty("scheme_Id")
    private Long schemeId;

    @JsonProperty("MF_Id")
    private Long mfId;
}
