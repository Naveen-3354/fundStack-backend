package com.test.FundStack.configuration;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import tools.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLInputFactory;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> ApplicationCOnfig
 *      - Initial Version.
 */


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
}
