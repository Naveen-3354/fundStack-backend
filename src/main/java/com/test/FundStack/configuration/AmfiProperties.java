package com.test.FundStack.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "amfi")
public class AmfiProperties {
    private String schemeList;
    private String schemeData;
    private String dailyNavData;
    private String navHistoryData;
    private String sifHistoryData;
    private String sifDailyNav;
    private String historyData;
    private String newFundOffer;
    private String schemeDetail;
    private String schemeDocs;
    private String schemeNavAndAum;
    private String schemeCSV;

    public Map<String, String> asMap() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("schemeList", schemeList);
        values.put("schemeData", schemeData);
        values.put("dailyNavData", dailyNavData);
        values.put("navHistoryData", navHistoryData);
        values.put("sifHistoryData", sifHistoryData);
        values.put("sifDailyNav", sifDailyNav);
        values.put("historyData", historyData);
        values.put("newFundOffer", newFundOffer);
        values.put("schemeDetail", schemeDetail);
        values.put("schemeDocs", schemeDocs);
        values.put("schemeNavAndAum", schemeNavAndAum);
        values.put("schemeCSV", schemeCSV);
        return values;
    }
}
