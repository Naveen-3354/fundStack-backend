package com.test.FundStack.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.FundStack.model.amfi.AmfiSchemeDetailApiResponse;
import com.test.FundStack.model.amfi.AmfiSchemeDetailResponse;
import com.test.FundStack.model.amfi.SchemeList;
import com.test.FundStack.model.amfi.SchemeNavResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

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
    
    @Value("${amfi.schemeList}")
    private String schemeListUrl;

    @Value("${amfi.schemeDetail}")
    private String schemeDetailUrl;

    @Value("${amfi.schemeNavAndAum}")
    private String schemeNavUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<SchemeList> getListOfSchemes(String amfiId) {

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
        String url = String.format(schemeDetailUrl, mfId, schemeId);
        AmfiSchemeDetailApiResponse response =
                getSchemeDetails(url, AmfiSchemeDetailApiResponse.class);
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.getData().getFirst());
    }

    public List<SchemeNavResponseDTO> fetchSchemeNavDetails(String mfId, String schemeId) {
        String url = String.format(schemeNavUrl, mfId, schemeId, "NAV");
        String json = restTemplate.getForObject(url, String.class);
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        List<SchemeNavResponseDTO> response = objectMapper.readValue(
                json,
                new TypeReference<>() {}
        );
        return response == null || response.isEmpty() ? Collections.emptyList() : response;
    }


    public <T> T getSchemeDetails(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

}
