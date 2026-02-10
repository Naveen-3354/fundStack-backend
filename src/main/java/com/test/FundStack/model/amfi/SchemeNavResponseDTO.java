package com.test.FundStack.model.amfi;


/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemeNavResponseDTO
 *      - Initial Version.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeNavResponseDTO {

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

    @JsonProperty("strMFId")
    private Long mfId;

    @JsonProperty("strOption")
    private String option;

    @JsonProperty("strSDId")
    private Long sdId;
}
