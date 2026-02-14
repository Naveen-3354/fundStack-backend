package com.test.FundStack.model.xml;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -11-02-2026 <NaveenDhanasekaran> SchemeSummaryDocument
 *      - Initial Version.
 */

@Data
@JsonRootName("SchemeSummaryDocument")
public class SchemeSummaryDocument {

    @JacksonXmlProperty(localName = "SchemeSummary")
    private SchemeSummary schemeSummary;
}