package com.test.FundStack.service;


import com.test.FundStack.entity.*;
import com.test.FundStack.enums.ImportStatus;
import com.test.FundStack.model.PaginationResponse;
import com.test.FundStack.model.schemeMigration.SchemeGroupedDto;
import com.test.FundStack.model.schemeMigration.SchemeNavDto;
import com.test.FundStack.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -14-02-2026 <NaveenDhanasekaran> SchemeCsvService
 *      - Initial Version.
 */
 
@Slf4j
@Service
@RequiredArgsConstructor
public class SchemeCsvService {
    
    private final SchemeCsvRepository schemeCsvRepository;
    private final SchemeRepository schemeRepository;
    private final SchemePlanOptionRepo schemePlanOptionRepo;
    private final FundHouseRepo fundHouseRepository;
    private final CategoryRepo categoryRepo;
    
    public void migrateBySchemeName(String schemeName){
        List<SchemeCsv> schemeCsvs = schemeCsvRepository.findBySchemeName(schemeName);
        migrateSchemeCsv(schemeCsvs);
    }

    public void migrateByAMCName(String amcName){
        List<SchemeCsv> schemeCsvs = schemeCsvRepository.findByAmcName(amcName);
        migrateSchemeCsv(schemeCsvs);
    }

    public PaginationResponse<List<SchemeGroupedDto>> getGroupedSchemeCsv(String amcName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNo,
                pageSize,
                Sort.by(Sort.Direction.ASC, "schemeName")
        );
        Page<String> schemeNamePage =
                schemeCsvRepository.findDistinctSchemeNamesByAmc(amcName, pageable);
        List<SchemeCsv> records =
                schemeCsvRepository.findBySchemeNameIn(schemeNamePage.getContent());
        Map<String, List<SchemeCsv>> grouped =
                records.stream()
                        .collect(Collectors.groupingBy(SchemeCsv::getSchemeName));
        List<SchemeGroupedDto> result =
                grouped.entrySet().stream()
                        .map(entry -> new SchemeGroupedDto(
                                entry.getKey(),
                                entry.getValue().stream()
                                        .map(csv -> new SchemeNavDto(
                                                csv.getSchemeNavName(),
                                                csv.getCode(),
                                                csv.getIsinDivPayOut(),
                                                csv.getIsinGrowthAndReinvest(),
                                                csv.getLaunchDate(),
                                                csv.getScheme() != null
                                        ))
                                        .toList()
                        ))
                        .toList();

        return PaginationResponse.<List<SchemeGroupedDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalCount(schemeNamePage.getTotalElements())
                .totalPages(schemeNamePage.getTotalPages())
                .data(result)
                .build();
    }

    @Transactional
    public void migrateSchemeCsv(List<SchemeCsv> csvList) {
        for (SchemeCsv csv : csvList) {
            FundHouse fundHouse = fundHouseRepository
                    .findByName(csv.getAmcName())
                    .orElseGet(() -> fundHouseRepository.save(
                            FundHouse.builder()
                                    .name(csv.getAmcName())
                                    .build()
                    ));
            Category category = getOrCreateCategory(csv.getSchemeCategory());
            Scheme scheme = schemeRepository
                    .findByNameAndFundHouse(csv.getSchemeName(), fundHouse)
                    .orElseGet(() -> schemeRepository.save(
                            Scheme.builder()
                                    .name(csv.getSchemeName())
                                    .amfiId(csv.getCode())
                                    .fundHouse(fundHouse)
                                    .category(category)
                                    .build()
                    ));
            if (scheme.getCategory() == null) {
                scheme.setCategory(category);
            }
            SchemePlanOption option = SchemePlanOption.builder()
                    .schemeNavName(csv.getSchemeNavName())
                    .amfiCode(csv.getCode())
                    .planType(extractPlanType(csv.getSchemeNavName()))
                    .optionType(extractOptionType(csv.getSchemeNavName()))
                    .isinDivPayoutIsinGrowth(csv.getIsinDivPayOut())
                    .isinDivReinvestment(csv.getIsinGrowthAndReinvest())
                    .scheme(scheme)
                    .build();
            schemePlanOptionRepo.save(option);
            csv.setScheme(scheme);
            csv.setVersion(1);
            csv.setActive(true);
            csv.setStatus(ImportStatus.MATCHED);
        }
        schemeCsvRepository.saveAll(csvList);
    }


    private String extractPlanType(String navName) {
        if (navName == null) return null;
        if (navName.toLowerCase().contains("direct"))
            return "DIRECT";
        return "REGULAR";
    }

    private String extractOptionType(String navName) {
        if (navName == null) return null;
        String lower = navName.toLowerCase();
        if (lower.contains("growth"))
            return "GROWTH";
        if (lower.contains("idcw") || lower.contains("dividend"))
            return "IDCW";
        return null;
    }

    private String[] splitCategory(String category) {
        if (category == null || category.isBlank())
            return new String[]{null, null};

        String[] parts = category.split(" - ", 2);

        String parent = parts[0].trim();
        String child = parts.length > 1 ? parts[1].trim() : null;

        return new String[]{parent, child};
    }

    @Transactional
    public Category getOrCreateCategory(String categoryString) {

        String[] parts = splitCategory(categoryString);

        String parentName = parts[0];
        String childName = parts[1];

        if (parentName == null)
            return null;
        Category parent = categoryRepo
                .findByNameAndParentIsNull(parentName)
                .orElseGet(() -> categoryRepo.save(
                        Category.builder()
                                .name(parentName)
                                .build()
                ));

        if (childName == null)
            return parent;

        // 2️⃣ Find or create child
        return categoryRepo
                .findByNameAndParent(childName, parent)
                .orElseGet(() -> categoryRepo.save(
                        Category.builder()
                                .name(childName)
                                .parent(parent)
                                .build()
                ));
    }

}
