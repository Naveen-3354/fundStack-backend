package com.test.FundStack.model.xml;


import lombok.Data;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -11-02-2026 <NaveenDhanasekaran> SchemeSummary
 *      - Initial Version.
 */

@Data
public class SchemeSummary {

    @JacksonXmlProperty(localName = "Fund_Name")
    private String fundName;

    @JacksonXmlProperty(localName = "Option_Names_Regular__Direct")
    private String optionNamesRegularDirect;

    @JacksonXmlProperty(localName = "Fund_Type")
    private String fundType;

    @JacksonXmlProperty(localName = "Riskometer_At_the_time_of_Launch")
    private String riskoMeterAtTheTimeOfLaunch;

    @JacksonXmlProperty(localName = "Riskometer_as_on_Date")
    private String riskoMeterAsOnDate;

    @JacksonXmlProperty(localName = "Category_as_Per_SEBI_Categorization_Circular")
    private String category;

    @JacksonXmlProperty(localName = "Potential_Risk_Class_as_on_date")
    private String potentialRiskoMeter;

    @JacksonXmlProperty(localName = "Description_Objective_of_the_scheme")
    private String description;
    
    @JacksonXmlProperty(localName = "Stated_Asset_Allocation")
    private String stateOfAllocation;

    @JacksonXmlProperty(localName = "Face_Value")
    private String faceValue;

    @JacksonXmlProperty(localName = "NFO_Open_Date")
    private String nfoOpenDate;

    @JacksonXmlProperty(localName = "NFO_Close_date")
    private String nfoCloseDate;

    @JacksonXmlProperty(localName = "Allotment_Date")
    private String allotmentDate;

    @JacksonXmlProperty(localName = "Reopen_Date")
    private String reopenDate;

    @JacksonXmlProperty(localName = "Maturity_Date_For_closedend_funds")
    private String maturityDate;

    @JacksonXmlProperty(localName = "Benchmark_Tier_1")
    private String benchmarkTier1;

    @JacksonXmlProperty(localName = "Benchmark_Tier_2")
    private String benchmarkTier2;

    @JacksonXmlProperty(localName = "Fund_Manager_Name")
    private String fundManagerName;

    @JacksonXmlProperty(localName = "Fund_Manager_Type_PrimaryComanageDescription")
    private String fundManagerNameDescription;
    
    @JacksonXmlProperty(localName = "Fund_Manager_From_Date")
    private String fundManagerFromDate;

    @JacksonXmlProperty(localName = "Annual_Expense_Stated_maximum")
    private String annualExpense;

    @JacksonXmlProperty(localName = "Exit_Load_if_applicable")
    private String exitLoad;
    
    @JacksonXmlProperty(localName = "Custodian")
    private String custodian;

    @JacksonXmlProperty(localName = "Auditor")
    private String auditor;

    @JacksonXmlProperty(localName = "Registrar")
    private String registrar;
    
    @JacksonXmlProperty(localName = "RTA_Code_To_be_phased_out")
    private String rtaSchemeCode;

    @JacksonXmlProperty(localName = "Listing_Details")
    private String listingDetails;

    @JacksonXmlProperty(localName = "ISINs")
    private String isins;

    @JacksonXmlProperty(localName = "AMFI_Codes_To_be_phased_out")
    private String amfiCodes;

    @JacksonXmlProperty(localName = "SEBI_Codes")
    private String sebiCodes;

    @JacksonXmlProperty(localName = "Minimum_Application_Amount")
    private BigDecimal minimumAppAmount;

    @JacksonXmlProperty(localName = "Minimum_Application_Amount_in_multiples_of_Rs")
    private String minimumAppAmountInMul;

    @JacksonXmlProperty(localName = "Minimum_Additional_Amount")
    private String minimumAddAmount;

    @JacksonXmlProperty(localName = "Minimum_Additional_Amount_in_multiples_of_Rs")
    private String minimumAddAmountInMul;

    @JacksonXmlProperty(localName = "Minimum_Redemption_Amount_in_Rs")
    private String minRedAmt;

    @JacksonXmlProperty(localName = "Minimum_Redemption_Amount_in_Units")
    private String minRedUnt;

    @JacksonXmlProperty(localName = "Minimum_Balance_Amount_if_applicable")
    private String minBalAmt;

    @JacksonXmlProperty(localName = "Minimum_Balance_Amount_in_Units_if_applicable")
    private String minBalUnt;

    @JacksonXmlProperty(localName = "Max_Investment_Amount")
    private String macIntAmt;

    @JacksonXmlProperty(localName = "Minimum_Switch_Amount_if_applicable")
    private String minSwtAmt;

    @JacksonXmlProperty(localName = "Minimum_Switch_Units")
    private String minSwtUnt;

    @JacksonXmlProperty(localName = "Switch_Multiple_Amount_if_applicable")
    private String swtMulAmt;

    @JacksonXmlProperty(localName = "Switch_Multiple_Units_if_applicable")
    private String swtMulUnt;

    @JacksonXmlProperty(localName = "Max_Switch_Amount")
    private String maxSwtAmt;

    @JacksonXmlProperty(localName = "Max_Switch_Units_if_applicable")
    private String macSwtUnt;

    @JacksonXmlProperty(localName = "Swing_Pricing_if_applicable")
    private String swgPrice;

    @JacksonXmlProperty(localName = "Sidepocketing_if_applicable")
    private String sidePocketing;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_Frequency")
    private String sysFreq;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_Minimum_amount")
    private String sysMinAmt;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_In_multiple_of")
    private String sysMulp;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_Minimum_Instalments")
    private String sysMinInts;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_Dates")
    private String sysDates;

    @JacksonXmlProperty(localName = "SIP_SWP__STP_Details_Maximum_Amount_if_any")
    private String sysMaxAmt;    
    
}