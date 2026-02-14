package com.test.FundStack.model.amfi;


import lombok.Getter;
import lombok.Setter;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -11-02-2026 <NaveenDhanasekaran> SchemeDetailsDocsResoponse
 *      - Initial Version.
 */
 
@Getter
@Setter
public class SchemeDetailsDocsResoponse {
    private String schemeId;
    private String infoDocumentUrl;
    private String summaryPdfUrl;
    private String summaryXlsUrl;
    private String summaryXmlUrl;
}
