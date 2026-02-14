package com.test.FundStack.model;


import lombok.Builder;
import lombok.Data;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -10-02-2026 <NaveenDhanasekaran> PaginationResponse
 *      - Initial Version.
 */
 
@Data
@Builder
public class PaginationResponse<T> {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalCount;
    private T data;
}
