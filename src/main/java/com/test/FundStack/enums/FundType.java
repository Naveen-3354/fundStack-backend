package com.test.FundStack.enums;

import lombok.Getter;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -08-02-2026 <NaveenDhanasekaran> FundType
 * - Initial Version.
 */

@Getter
public enum FundType {

    MF("Mutual Fund"),
    SIF("Specialised Investment Fund");

    private final String description;

    FundType(String description) {
        this.description = description;
    }
}
