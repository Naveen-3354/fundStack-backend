package com.test.FundStack.service;


import com.test.FundStack.entity.Scheme;
import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.model.amfi.SchemeNavResponseDTO;
import com.test.FundStack.model.xml.SchemeSummaryDocument;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.SchemePlanOptionRepo;
import com.test.FundStack.repository.SchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemePlanOptionSerivce
 *      - Initial Version.
 */
 
@Slf4j
@Service
public class SchemePlanOptionService extends CrudService<SchemePlanOption, Long> {
    
    private final SchemePlanOptionRepo schemePlanOptionRepo;
    private final SchemeRepository schemeRepository;
    private final AmfiService amfiService;
    
    protected SchemePlanOptionService(CrudRepositoryBase<SchemePlanOption, Long> repository,
                                      SchemePlanOptionRepo schemePlanOptionRepo,
                                      SchemeRepository schemeRepository,
                                      AmfiService amfiService) {
        super(repository);
        this.schemePlanOptionRepo = schemePlanOptionRepo;
        this.schemeRepository = schemeRepository;
        this.amfiService = amfiService;
    }

    @Transactional
    public void saveAllNavDetails(long schemeId) {
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new EntityNotFoundException("Scheme not found: " + schemeId));
        List<SchemeNavResponseDTO> responses =
                amfiService.fetchSchemeNavDetails(
                        scheme.getFundHouse().getAmfiId(),
                        scheme.getAmfiId(),
                        "NAV"
                );
        if (responses == null || responses.isEmpty()) {
            return;
        }
        Set<String> existingIsins =
                schemePlanOptionRepo.findExistingIsinsBySchemeId(schemeId);
        List<SchemePlanOption> newOptions = responses.stream()
                .filter(dto -> !existingIsins.contains(dto.getIsinDivPayoutIsinGrowth()))
                .map(dto -> extractOptionType(SchemePlanOption.builder()
                        .schemeNavName(dto.getSchemeNavName())
                        .isinDivPayoutIsinGrowth(dto.getIsinDivPayoutIsinGrowth())
                        .isinDivReinvestment(dto.getIsinDivReinvestment())
                        .scheme(scheme)
                        .build())
                )
                .toList();
        if (!newOptions.isEmpty()) {
            schemePlanOptionRepo.saveAll(newOptions);
        }
    }

    private SchemePlanOption extractOptionType(SchemePlanOption schemePlanOption) {
        String schemeName = schemePlanOption.getSchemeNavName().toLowerCase();
        if (schemeName.contains("Regular".toLowerCase())) schemePlanOption.setPlanType("Regular");
        if (schemeName.contains("direct".toLowerCase())) schemePlanOption.setPlanType("Direct");
        if (schemeName.contains("IDCW".toLowerCase())) schemePlanOption.setOptionType("IDCW");
        if (schemeName.contains("Growth".toLowerCase())) schemePlanOption.setOptionType("Growth");
        return schemePlanOption;
    }

    @Transactional
    public List<SchemePlanOption> getAllNavDetailsByScheme(long schemeId){
        return schemePlanOptionRepo.findBySchemeId(schemeId);
    }
    
    public SchemeSummaryDocument getXmlData(String schemeId){
        return amfiService.fetchSchemeDetailsDocsXml(schemeId);
    }
}
