package com.test.FundStack.service;


import com.test.FundStack.entity.AmfiSchemeDetails;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.model.amfi.AmfiSchemeDetailResponse;
import com.test.FundStack.model.amfi.SchemeNavResponseDTO;
import com.test.FundStack.repository.AmfiSchemeDetailsRepo;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.SchemePlanOptionRepo;
import com.test.FundStack.repository.SchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemeDetailsService
 *      - Initial Version.
 */
 
@Service
public class SchemeDetailsService extends CrudService<AmfiSchemeDetails, Long>{

    private final AmfiSchemeDetailsRepo amfiSchemeDetailsRepo;
    private final SchemePlanOptionRepo schemePlanOptionRepo;
    private final AmfiService amfiService;
    private final SchemeRepository schemeRepository;

    protected SchemeDetailsService(
            CrudRepositoryBase<AmfiSchemeDetails, Long> repository,
            AmfiSchemeDetailsRepo amfiSchemeDetailsRepo,
            AmfiService amfiService,
            SchemePlanOptionRepo schemePlanOptionRepo,
            SchemeRepository schemeRepository
    ) {
        super(repository);
        this.amfiSchemeDetailsRepo = amfiSchemeDetailsRepo;
        this.amfiService = amfiService;
        this.schemePlanOptionRepo = schemePlanOptionRepo;
        this.schemeRepository = schemeRepository;
    }
    
    public AmfiSchemeDetails syncSchemeDetails(long schemeId){
        return schemeRepository.findById(schemeId)
                .map(scheme -> saveSchemeDetails(
                        scheme,
                        scheme.getFundHouse().getAmfiId(),
                        scheme.getAmfiId()
                ))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Scheme not found for id: " + schemeId
                ));
    }

    @Transactional
    public AmfiSchemeDetails saveSchemeDetails(
            Scheme scheme,
            String mfId,
            String schemeId
    ) {
        AmfiSchemeDetailResponse response =
                amfiService.fetchSchemeDetails(mfId, schemeId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "AMFI scheme details not found for schemeId: " + schemeId
                                )
                        );
        AmfiSchemeDetails details = amfiSchemeDetailsRepo.findBySchemeId(scheme.getId())
                .orElseGet(AmfiSchemeDetails::new);
        details.setMfName(response.getMfName());
        details.setSchemeName(response.getSchemeName());
        details.setSchemeObjective(response.getSchemeObjective());
        details.setSchemeTypeDesc(response.getSchemeTypeDesc());
        details.setSchemeCatDesc(response.getSchemeCatDesc());
        details.setSchemeLoad(response.getSchemeLoad());
        details.setSchemeMinAmt(response.getSchemeMinAmt());
        details.setLaunchDate(response.getLaunchDate());
        details.setAmcWebsite(response.getAmcWebsite());
        details.setScheme(scheme);
        return amfiSchemeDetailsRepo.save(details);
    }

    @Transactional
    public Optional<AmfiSchemeDetails> getSchemeDetailsBySchemeId(long schemeId){
        return amfiSchemeDetailsRepo.findBySchemeId(schemeId);
    }

}
