package com.test.FundStack.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.FundStack.model.amfi.AmfiSchemeDetailApiResponse;
import com.test.FundStack.model.amfi.AmfiSchemeDetailResponse;
import com.test.FundStack.model.amfi.SchemeList;
import com.test.FundStack.model.amfi.SchemeNavResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> amfiService
 *      - Initial Version.
 */
 
@Service
@RequiredArgsConstructor
public class AmfiService {

    private static final String KEY_SCHEME_LIST = "schemeList";
    private static final String KEY_SCHEME_DETAIL = "schemeDetail";
    private static final String KEY_SCHEME_NAV_AUM = "schemeNavAndAum";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AmfiUrlConfigService amfiUrlConfigService;

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
        AmfiSchemeDetailApiResponse response =
                getSchemeDetails(url, AmfiSchemeDetailApiResponse.class);
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.getData().getFirst());
    }

    public List<SchemeNavResponseDTO> fetchSchemeNavDetails(String mfId, String schemeId) {
        String schemeNavUrl = amfiUrlConfigService.getUrlByKey(KEY_SCHEME_NAV_AUM);
        String url = String.format(schemeNavUrl, mfId, schemeId, "NAV");
        String json = restTemplate.getForObject(url, String.class);
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        List<SchemeNavResponseDTO> response;
        try {
            response = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse AMFI NAV response", e);
        }
        return response == null || response.isEmpty() ? Collections.emptyList() : response;
    }


    public <T> T getSchemeDetails(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

}
