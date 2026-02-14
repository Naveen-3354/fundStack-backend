package com.test.FundStack.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> ApiResponse
 *      - Initial Version.
 */

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
