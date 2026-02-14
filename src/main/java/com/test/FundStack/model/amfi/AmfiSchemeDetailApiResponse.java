package com.test.FundStack.model.amfi;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> AmfiSchemeDetailApiResponse
 *      - Initial Version.
 */

@Data
public class AmfiSchemeDetailApiResponse <T> {

    @JsonProperty("data")
    private List<T> data;
}

