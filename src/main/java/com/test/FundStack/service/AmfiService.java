package com.test.FundStack.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.FundStack.entity.*;
import com.test.FundStack.enums.ImportStatus;
import com.test.FundStack.model.amfi.*;
import com.test.FundStack.model.xml.SchemeSummaryDocument;
import com.test.FundStack.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> amfiService
 *      - Initial Version.
 */
 
@Slf4j
@Service
@RequiredArgsConstructor
public class AmfiService {

    private static final String KEY_SCHEME_LIST = "schemeList";
    private static final String KEY_SCHEME_DETAIL = "schemeDetail";
    private static final String KEY_SCHEME_NAV_AUM = "schemeNavAndAum";
    private static final String KEY_SCHEME_DOCS = "schemeDocs";
    private static final String KEY_SCHEME_DOCS_XML = "schemeDocsXml";
    private static final String KEY_SCHEME_CSV = "schemeCSV";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmfiUrlConfigService amfiUrlConfigService;
    private final XmlMapper xmlMapper;
    private final CategoryRepo categoryRepo;
    private final FundHouseRepo fundHouseRepo;
    private final SchemeRepository schemeRepository;
    private final SchemePlanOptionRepo schemePlanOptionRepo;
    private final SchemeCsvRepository schemeCsvRepository;

    public List<SchemeList> getListOfSchemes(String amfiId) {
        String schemeListUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_LIST);
        ResponseEntity<List<SchemeList>> response =
                restTemplate.exchange(
                        schemeListUrl + amfiId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        return response.getBody();
    }

    public Optional<AmfiSchemeDetailResponse> fetchSchemeDetails(String mfId, String schemeId) {
        String schemeDetailUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_DETAIL);
        String url = String.format(schemeDetailUrl, mfId, schemeId);
        ResponseEntity<AmfiSchemeDetailApiResponse<AmfiSchemeDetailResponse>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        AmfiSchemeDetailApiResponse<AmfiSchemeDetailResponse> responseBody = response.getBody();
        assert responseBody != null;
        return Optional.ofNullable(responseBody.getData().getFirst());
    }

    public List<SchemeNavResponseDTO> fetchSchemeNavDetails(String mfId, String schemeId, String option) {
        String schemeNavUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_NAV_AUM);
        String url = String.format(schemeNavUrl, mfId, schemeId, option);
        String json = getSchemeDetails(url, String.class);
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        List<SchemeNavResponseDTO> response;
        try {
            response = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Unable to parse AMFI NAV response: %s", e);
            return null;
        }
        return response == null || response.isEmpty() ? Collections.emptyList() : response;
    }

    public Optional<SchemeDetailsDocsResoponse> fetchSchemeDetailsDocs(String schemeId) {
        String schemeDetailUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_DOCS);
        String url = String.format(schemeDetailUrl, schemeId);
        AmfiSchemeDetailApiResponse<SchemeDetailsDocsResoponse> response =
                getSchemeDetails(url, AmfiSchemeDetailApiResponse.class);
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.getData().getFirst());
    }

    public SchemeSummaryDocument fetchSchemeDetailsDocsXml(String schemeId) {
        String schemeDetailUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_DOCS_XML);
        String url = String.format(schemeDetailUrl, schemeId);
        Optional<SchemeSummaryDocument> response =
                fetchAndParse(url, SchemeSummaryDocument.class);
        return response.orElse(null);
    }

    public <T> Optional<T> fetchAndParse(String url, Class<T> responseType) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || response.getBody().length == 0) {
                return Optional.empty();
            }
            try (InputStream inputStream = new ByteArrayInputStream(response.getBody())) {
                T parsed = xmlMapper.readValue(inputStream, responseType);
                return Optional.ofNullable(parsed);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            log.warn("AMFI XML not found: {}", url);
            return Optional.empty();
        } catch (Exception ex) {
            log.error("Failed to fetch or parse AMFI XML from {}", url, ex);
            throw new IllegalStateException("Failed to process AMFI XML response", ex);
        }
    }

    @Transactional
    public void fetchAndImportCsv() throws Exception {
        String url = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_CSV);
        byte[] response = restTemplate.getForObject(url, byte[].class);
        if (response == null || response.length == 0) {
            throw new RuntimeException("Empty response from AMFI URL");
        }
        LocalDate importDate = LocalDate.now();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(response)))) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();
            CSVParser parser = new CSVParser(reader, format);
            List<SchemeCsv> batch = new ArrayList<>();
            for (CSVRecord record : parser) {
                String code = record.get("Code");
                boolean exists = schemeCsvRepository
                        .existsByCodeAndImportDate(code, importDate);
                if (exists) {
                    continue;
                }
                String amc = record.get("AMC");
                String schemeName = record.get("Scheme Name");
                String schemeType = record.get("Scheme Type");
                String schemeCategory = record.get("Scheme Category");
                String schemeNAVName = record.get("Scheme NAV Name");
                String schemeMinimumAmount = record.get("Scheme Minimum Amount");
                String launchDateStr = record.get("Launch Date");
                String closureDateStr = record.get("Closure Date");
                String isins = record.get("ISIN Div Payout/ ISIN GrowthISIN Div Reinvestment");
                LocalDate launchDate = null;
                LocalDate closureDate = null;
                if (launchDateStr != null && !launchDateStr.isBlank()) {
                    launchDate = parseDate(launchDateStr);
                }
                if (closureDateStr != null && !closureDateStr.isBlank()) {
                    closureDate = parseDate(closureDateStr);
                }
                String isinDivPayout = null;
                String isinGrowthReinvest = null;
                if (isins != null && !isins.isBlank()) {
                    if (isins.length() > 12) {
                        isinDivPayout = isins.substring(0, 12);
                        isinGrowthReinvest = isins.substring(12);
                    } else {
                        isinGrowthReinvest = isins;
                    }
                }
                SchemeCsv entity = SchemeCsv.builder()
                        .amcName(amc)
                        .code(code)
                        .schemeName(schemeName)
                        .schemeType(schemeType)
                        .schemeCategory(schemeCategory)
                        .schemeNavName(schemeNAVName)
                        .schemeMinAmt(schemeMinimumAmount)
                        .launchDate(launchDate)
                        .closureDate(closureDate)
                        .isinDivPayOut(isinDivPayout)
                        .isinGrowthAndReinvest(isinGrowthReinvest)
                        .importDate(importDate)
                        .active(false)
                        .status(ImportStatus.NEW)
                        .build();
                batch.add(entity);
            }
            schemeCsvRepository.saveAll(batch);
        }
    }
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }
    
    public <T> T getSchemeDetails(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

}
