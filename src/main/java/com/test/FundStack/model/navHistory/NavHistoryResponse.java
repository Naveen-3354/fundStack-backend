package com.test.FundStack.model.navHistory;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -28-02-2026 <NaveenDhanasekaran> NavHistoryResponse
 *      - Initial Version.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavHistoryResponse {

    private DataBlock data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBlock {
        @JsonProperty("mf_name")
        private String mfName;

        @JsonProperty("scheme_name")
        private String schemeName;

        @JsonProperty("date_range")
        private String dateRange;

        @JsonProperty("nav_groups")
        private List<NavGroup> navGroups;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NavGroup {
        @JsonProperty("nav_name")
        private String navName;

        @JsonProperty("historical_records")
        private List<HistoricalRecord> historicalRecords;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoricalRecord {
        private String date;
        private String nav;

        @JsonProperty("upload_time")
        private String uploadTime;

        @JsonProperty("upload_time_display")
        private String uploadTimeDisplay;

        private String repurchase;
        private String reissue;
    }
    
}
