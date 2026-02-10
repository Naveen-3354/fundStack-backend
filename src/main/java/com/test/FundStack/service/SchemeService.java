package com.test.FundStack.service;


import com.test.FundStack.entity.FundHouse;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.model.amfi.SchemeCreatedEvent;
import com.test.FundStack.model.amfi.SchemeList;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.FundHouseRepo;
import com.test.FundStack.repository.SchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> SchemeService
 *      - Initial Version.
 */

@Service
public class SchemeService extends CrudService<Scheme, Long> {

    private final AmfiService amfiService;
    private final FundHouseRepo fundHouseRepo;
    private final SchemeRepository schemeRepository;
    private final SchemeOrchestrationService schemeOrchestrationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SchemeService(
            CrudRepositoryBase<Scheme, Long> repository,
            AmfiService amfiService,
            FundHouseRepo fundHouseRepo,
            SchemeRepository schemeRepository,
            SchemeOrchestrationService schemeOrchestrationService,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        super(repository);
        this.amfiService = amfiService;
        this.fundHouseRepo = fundHouseRepo;
        this.schemeRepository = schemeRepository;
        this.schemeOrchestrationService = schemeOrchestrationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public List<Scheme> getSchemesList(String amfiId) {

        FundHouse fundHouse = fundHouseRepo.findByAmfiId(amfiId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "FundHouse not found for AMFI ID: " + amfiId
                ));

        List<SchemeList> schemeLists = amfiService.getListOfSchemes(amfiId);

        if (schemeLists.isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> existingAmfiIds = schemeRepository
                .findByFundHouseId(fundHouse.getId())
                .stream()
                .map(Scheme::getAmfiId)
                .collect(Collectors.toSet());
        List<Scheme> schemesToSave = schemeLists.stream()
                .filter(s -> !existingAmfiIds.contains(s.getSchemeId()))
                .map(s -> Scheme.builder()
                        .amfiId(s.getSchemeId())
                        .name(s.getSchemeName())
                        .fundHouse(fundHouse)
                        .build()
                )
                .toList();
        if (schemesToSave.isEmpty()) {
            return Collections.emptyList();
        }
        List<Scheme> savedSchemes = saveAll(schemesToSave);
        savedSchemes.forEach(schemeOrchestrationService::handleSchemeCreated);
        return savedSchemes;
    }

}

