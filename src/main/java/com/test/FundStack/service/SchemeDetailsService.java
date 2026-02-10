package com.test.FundStack.service;


import com.test.FundStack.entity.AmfiSchemeDetails;
import com.test.FundStack.entity.Scheme;
import com.test.FundStack.entity.SchemePlanOption;
import com.test.FundStack.model.amfi.AmfiSchemeDetailResponse;
import com.test.FundStack.model.amfi.SchemeNavResponseDTO;
import com.test.FundStack.repository.AmfiSchemeDetailsRepo;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.SchemePlanOptionRepo;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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

    protected SchemeDetailsService(
            CrudRepositoryBase<AmfiSchemeDetails, Long> repository,
            AmfiSchemeDetailsRepo amfiSchemeDetailsRepo,
            AmfiService amfiService,
            SchemePlanOptionRepo schemePlanOptionRepo
    ) {
        super(repository);
        this.amfiSchemeDetailsRepo = amfiSchemeDetailsRepo;
        this.amfiService = amfiService;
        this.schemePlanOptionRepo = schemePlanOptionRepo;
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
        List<SchemeNavResponseDTO> schemeNavResponseDTOS = amfiService.fetchSchemeNavDetails(mfId, schemeId);
        if(!schemeNavResponseDTOS.isEmpty()){
            schemePlanOptionRepo.save(SchemePlanOption.builder()
                    .build());
        }
                    AmfiSchemeDetails details = AmfiSchemeDetails.builder()
                            .mfName(response.getMfName())
                            .schemeName(response.getSchemeName())
                            .schemeObjective(response.getSchemeObjective())
                            .schemeTypeDesc(response.getSchemeTypeDesc())
                            .schemeCatDesc(response.getSchemeCatDesc())
                            .schemeLoad(response.getSchemeLoad())
                            .schemeMinAmt(response.getSchemeMinAmt())
                            .launchDate(response.getLaunchDate())
                            .amcWebsite(response.getAmcWebsite())
                            .scheme(scheme)
                            .build();

                    return amfiSchemeDetailsRepo.save(details);
    }

}
