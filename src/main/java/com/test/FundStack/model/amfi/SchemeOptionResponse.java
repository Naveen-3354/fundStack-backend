package com.test.FundStack.model.amfi;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemeOptionResponse
 *      - Initial Version.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeOptionResponse {

    @JsonProperty("Scheme_NAV_Name")
    private String schemeNavName;

    @JsonProperty("ISIN_Div_Payout_ISIN_Growth")
    private String isinDivPayoutIsinGrowth;

    @JsonProperty("ISIN_Div_Reinvestment")
    private String isinDivReinvestment;

    @JsonProperty("Net_Asset_Value")
    private BigDecimal netAssetValue;

    @JsonProperty("Repurchase_Price")
    private BigDecimal repurchasePrice;

    @JsonProperty("Sales_Price")
    private BigDecimal salesPrice;

    @JsonProperty("Date")
    private OffsetDateTime date;

    @JsonProperty("Average_AUM_For_The_Quarter")
    private BigDecimal averageAumForTheQuarter;

    @JsonProperty("As_At_The_End_Of")
    private String asAtTheEndOf;

    @JsonProperty("strMFId")
    private Long mfId;

    @JsonProperty("strOption")
    private String option;

    @JsonProperty("strSDId")
    private Long sdId;
}
