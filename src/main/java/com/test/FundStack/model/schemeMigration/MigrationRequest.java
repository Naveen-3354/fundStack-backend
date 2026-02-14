package com.test.FundStack.model.schemeMigration;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> MigrationRequest
 *      - Initial Version.
 */

@Getter
@Setter
public class MigrationRequest {

    @NotBlank(message = "Value must not be blank")
    private String value;
}
