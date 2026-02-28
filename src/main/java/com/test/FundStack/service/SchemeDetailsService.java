package com.test.FundStack.service;

import com.test.FundStack.entity.*;
import com.test.FundStack.model.amfi.AmfiSchemeDetailResponse;
import com.test.FundStack.model.xml.SchemeSummary;
import com.test.FundStack.model.xml.SchemeSummaryDocument;
import com.test.FundStack.repository.SchemeDetailsRepo;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.SchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -09-02-2026 <NaveenDhanasekaran> SchemeDetailsService
 *      - Initial Version.
 */
 
@Service
public class SchemeDetailsService extends CrudService<SchemeDetails, Long>{

    private final SchemeDetailsRepo amfiSchemeDetailsRepo;
    private final AmfiService amfiService;
    private final SchemeRepository schemeRepository;

    protected SchemeDetailsService(
            CrudRepositoryBase<SchemeDetails, Long> repository,
            SchemeDetailsRepo amfiSchemeDetailsRepo,
            AmfiService amfiService,
            SchemeRepository schemeRepository
    ) {
        super(repository);
        this.amfiSchemeDetailsRepo = amfiSchemeDetailsRepo;
        this.amfiService = amfiService;
        this.schemeRepository = schemeRepository;
    }
    
    public SchemeDetails syncSchemeDetails(long schemeId){
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
    
    public SchemeDetails  saveSchemeDetails(
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
        SchemeDetails details = amfiSchemeDetailsRepo.findBySchemeId(scheme.getId())
                .orElseGet(SchemeDetails::new);
        details.setMfName(response.getMfName());
        details.setSchemeName(response.getSchemeName());
        details.setSchemeObjective(response.getSchemeObjective());
        details.setSchemeCatDesc(response.getSchemeCatDesc());
        details.setEntryLoad(response.getSchemeLoad());
        details.setSchemeMinAmt(response.getSchemeMinAmt());
        details.setLaunchDate(response.getLaunchDate().toLocalDate());
        details.setAmcWebsite(response.getAmcWebsite());
        details.setScheme(scheme);
        scheme.setBasicDetailsSynced(true);
        details = amfiSchemeDetailsRepo.save(details);
        schemeRepository.save(scheme);
        loadDetailsFromXMl(details, scheme);
        return details;
    }

    private void loadDetailsFromXMl(SchemeDetails details, Scheme scheme) {
        SchemeSummaryDocument document = amfiService.fetchSchemeDetailsDocsXml(scheme.getAmfiId());
        if(document != null){
            SchemeSummary summary = document.getSchemeSummary();
            details.setAllotmentDate(parseToLocalDate(summary.getAllotmentDate()));
            setAnnualExpenses(summary, details);
            details.setAuditor(summary.getAuditor());
            details.setBenchmark1(summary.getBenchmarkTier1());
            details.setBenchmark2(summary.getBenchmarkTier2());
            details.setCustodian(summary.getCustodian());
            details.setDescription(summary.getDescription());
            details.setExitLoad(summary.getExitLoad());
            details.setFaceValue(summary.getFaceValue());
            details.setMaturityDate(parseToLocalDate(summary.getMaturityDate()));
            details.setNfoCloseDate(parseToLocalDate(summary.getNfoCloseDate()));
            details.setNfoOpenDate(parseToLocalDate(summary.getNfoOpenDate()));
            details.setPotentialRiskoMeter(summary.getPotentialRiskoMeter());
            details.setRegistrar(summary.getRegistrar());
            details.setReopenDate(parseToLocalDate(summary.getReopenDate()));
            details.setRiskoMeterAsOnDate(summary.getRiskoMeterAsOnDate());
            details.setRiskoMeterAtLaunch(summary.getRiskoMeterAtTheTimeOfLaunch());
            details.setSidePocketing(summary.getSidePocketing());
            details.setAssetAllocation(summary.getStateOfAllocation());
            details = amfiSchemeDetailsRepo.save(details);
            scheme.setXmlDetailsSynced(true);
            schemeRepository.save(scheme);
            loadTransaction(details, summary);
            loadPlanOptions(details, summary);
            loadManagers(scheme, summary);
        }
    }

    private void loadManagers(Scheme scheme, SchemeSummary summary) {

        // 1️⃣ Split fields safely
        List<String> names = Arrays.stream(
                        Optional.ofNullable(summary.getFundManagerName()).orElse("")
                                .split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> fromDates = Arrays.stream(
                        Optional.ofNullable(summary.getFundManagerFromDate()).orElse("")
                                .split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> descriptions = Arrays.stream(
                        Optional.ofNullable(summary.getFundManagerNameDescription()).orElse("")
                                .split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // Determine limit to avoid IndexOutOfBounds
        int limit = Math.min(Math.min(names.size(), fromDates.size()), descriptions.size());

        for (int i = 0; i < limit; i++) {

            String name = names.get(i);
            String fromDateStr = fromDates.get(i);
            String description = descriptions.get(i);

            // Parse role from description
            String role = null;
            if (description.contains("-")) {
                String[] parts = description.split("-", 2);
                if (parts.length == 2) {
                    role = parts[1].trim();  // e.g., "Primary/Equity"
                }
            }

            // Parse fromDate (dd MMM yyyy)
            LocalDate fromDate = null;
            try {
                fromDate = LocalDate.parse(fromDateStr, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
            } catch (DateTimeParseException e) {
                // ignore or log
            }

            // Create FundManager (ideally check DB first, simplified here)
            FundManager fundManager = FundManager.builder()
                    .name(name)
                    .build();

            // Create SchemeManager
            SchemeManager schemeManager = SchemeManager.builder()
                    .scheme(scheme)
                    .fundManager(fundManager)
                    .role(role)
                    .fromDate(fromDate)
                    .build();

            // Add to Scheme
            if (scheme.getSchemeManagers() == null) {
                scheme.setSchemeManagers(new ArrayList<>());
            }
            scheme.getSchemeManagers().add(schemeManager);
        }
    }

    private void loadPlanOptions(SchemeDetails details, SchemeSummary summary) {

        List<SchemePlanOption> schemePlanOptionList =
                details.getScheme().getPlanOptions();
        if (schemePlanOptionList == null || schemePlanOptionList.isEmpty()) {
            return;
        }
        List<String> rtaSchemeCodes = Arrays.stream(
                        Optional.ofNullable(summary.getRtaSchemeCode()).orElse("")
                                .trim()
                                .split("[,\\s]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        List<String> amfiCodes = Arrays.stream(
                        Optional.ofNullable(summary.getAmfiCodes()).orElse("")
                                .trim()
                                .split("[,\\s]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        Map<String, String> amfiToRtaMap = new HashMap<>();
        int limit = Math.min(amfiCodes.size(), rtaSchemeCodes.size());
        for (int i = 0; i < limit; i++) {
            amfiToRtaMap.put(amfiCodes.get(i), rtaSchemeCodes.get(i));
        }
        for (SchemePlanOption option : schemePlanOptionList) {
            if (option.getRtaSchemeCode() == null) {
                String rtaCode = amfiToRtaMap.get(option.getAmfiCode());
                if (rtaCode != null) {
                    option.setRtaSchemeCode(rtaCode);
                }
            }
        }
    }

    private void loadTransaction(SchemeDetails details, SchemeSummary summary) {
        Scheme scheme = details.getScheme();

        SchemeTransactions schemeTransactions = scheme.getSchemeTransactions();

        if (schemeTransactions == null) {
            schemeTransactions = SchemeTransactions.builder()
                    .minRedAmt(parseBigDecimal(summary.getMinRedAmt()))
                    .minRedUnit(parseUnit(summary.getMinRedUnt()))
                    .minBalAmt(parseBigDecimal(summary.getMinBalAmt()))
                    .minBalUnit(parseUnit(summary.getMinBalUnt()))
                    .minSwtAmt(parseBigDecimal(summary.getMinSwtAmt()))
                    .minSwtUnit(parseUnit(summary.getMinSwtUnt()))
                    .minimumAddAmount(parseBigDecimal(summary.getMinimumAddAmount()))
                    .minimumAddAmountInMul(Integer.valueOf(summary.getMinimumAddAmountInMul()))
                    .minimumAppAmount(parseBigDecimal(String.valueOf(summary.getMinimumAppAmount())))
                    .minimumAppAmountInMul(Integer.valueOf(summary.getMinimumAppAmountInMul()))
                    .swgPrice(parseBigDecimal(summary.getSwgPrice()))
                    .swtMulAmt(parseBigDecimal(summary.getSwtMulAmt()))
                    .swtMulUnit(parseUnit(summary.getSwtMulUnt()))
                    .scheme(scheme)
                    .build();
            scheme.setSchemeTransactions(schemeTransactions);
        } else {
            // Update existing transaction
            schemeTransactions.setMinRedAmt(parseBigDecimal(summary.getMinRedAmt()));
            schemeTransactions.setMinRedUnit(parseUnit(summary.getMinRedUnt()));
            schemeTransactions.setMinBalAmt(parseBigDecimal(summary.getMinBalAmt()));
            schemeTransactions.setMinBalUnit(parseUnit(summary.getMinBalUnt()));
            schemeTransactions.setMinSwtAmt(parseBigDecimal(summary.getMinSwtAmt()));
            schemeTransactions.setMinSwtUnit(parseUnit(summary.getMinSwtUnt()));
            schemeTransactions.setMinimumAddAmount(parseBigDecimal(summary.getMinimumAddAmount()));
            schemeTransactions.setMinimumAddAmountInMul(Integer.valueOf(summary.getMinimumAddAmountInMul()));
            schemeTransactions.setMinimumAppAmount(parseBigDecimal(String.valueOf(summary.getMinimumAppAmount())));
            schemeTransactions.setMinimumAppAmountInMul(Integer.valueOf(summary.getMinimumAppAmountInMul()));
            schemeTransactions.setSwgPrice(parseBigDecimal(summary.getSwgPrice()));
            schemeTransactions.setSwtMulAmt(parseBigDecimal(summary.getSwtMulAmt()));
            schemeTransactions.setSwtMulUnit(parseUnit(summary.getSwtMulUnt()));
        }

        schemeTransactions.getSystemTransactionRules().clear();
        List<SystemTransactionRule> rules = parseSystemTransactionRules(summary, schemeTransactions);
        schemeTransactions.getSystemTransactionRules().addAll(rules);

        scheme.setTransactionDetailsSynced(true);
        schemeRepository.save(scheme);
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.equalsIgnoreCase("NA")) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private SchemeTransactions.Unit parseUnit(String value) {
        if (value == null || value.equalsIgnoreCase("NA")) return SchemeTransactions.Unit.NA;
        if (value.equalsIgnoreCase("AMOUNT")) return SchemeTransactions.Unit.AMOUNT;
        if (value.equalsIgnoreCase("UNITS")) return SchemeTransactions.Unit.UNITS;
        return SchemeTransactions.Unit.NA;
    }

    private void setAnnualExpenses(SchemeSummary summary, SchemeDetails details) {
        String[] expenses = summary.getAnnualExpense().trim().split(",");
        for(String s: expenses){
            if(s.contentEquals("Regular")){
                details.setAnnualExpenseReg(s);
            }else{
                details.setAnnualExpenseDir(s);
            }
        }
    }

    private List<SystemTransactionRule> parseSystemTransactionRules(SchemeSummary summary,
                                                                    SchemeTransactions schemeTransactions) {

        List<SystemTransactionRule> rules = new ArrayList<>();

        // --- Parse sysMinAmt to get minAmount per type ---
        Map<String, BigDecimal> typeMinAmount = new HashMap<>();
        if (summary.getSysMinAmt() != null) {
            String[] lines = summary.getSysMinAmt().split("\n");
            for (String line : lines) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String typeStr = parts[0].trim(); // SIP/STP/SWP
                    BigDecimal minAmount = parseBigDecimal(parts[1].trim());
                    typeMinAmount.put(typeStr.toUpperCase(), minAmount);
                }
            }
        }

        // --- Parse sysMulp to get multiplier per type ---
        Map<String, Integer> typeMultiplier = new HashMap<>();
        if (summary.getSysMulp() != null) {
            String[] lines = summary.getSysMulp().split("\n");
            for (String line : lines) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String typeStr = parts[0].trim();
                    try {
                        int mul = Integer.parseInt(parts[1].trim());
                        typeMultiplier.put(typeStr.toUpperCase(), mul);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        // --- Parse sysMinInts to get minInstallments ---
        Map<String, Integer> typeMinInstallments = new HashMap<>();
        if (summary.getSysMinInts() != null) {
            String[] lines = summary.getSysMinInts().split("\n");
            for (String line : lines) {
                String[] parts = line.split("\\s+"); // e.g., "SIP - Weekly 6"
                if (parts.length >= 3) {
                    String typeStr = parts[0].trim(); // SIP
                    try {
                        int minInst = Integer.parseInt(parts[2].trim());
                        typeMinInstallments.put(typeStr.toUpperCase(), minInst);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        // --- Parse sysFreq and sysDates to get frequency + dayOrDates ---
        // For simplicity, map type -> frequency list
        Map<String, List<String>> typeFrequencyMap = new HashMap<>();
        Map<String, Map<String, List<String>>> typeFrequencyDatesMap = new HashMap<>();

        if (summary.getSysFreq() != null && summary.getSysDates() != null) {
            String[] freqLines = summary.getSysFreq().split("\n");
            String sysDates = summary.getSysDates();
            for (String line : freqLines) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String type = parts[0].trim().toUpperCase();
                    String freqs = parts[1].trim();
                    List<String> freqList = Arrays.stream(freqs.split(","))
                            .map(String::trim).toList();
                    typeFrequencyMap.put(type, freqList);

                    // --- Extract dayOrDates per frequency (optional detailed parsing) ---
                    // For now we can leave empty or parse sysDates based on type/freq
                    Map<String, List<String>> freqDates = new HashMap<>();
                    for (String freq : freqList) {
                        freqDates.put(freq, new ArrayList<>()); // optional detailed parse
                    }
                    typeFrequencyDatesMap.put(type, freqDates);
                }
            }
        }

        // --- Build SystemTransactionRule objects ---
        for (String type : typeMinAmount.keySet()) {
            BigDecimal minAmount = typeMinAmount.get(type);
            Integer multiplier = typeMultiplier.getOrDefault(type, null);
            Integer minInst = typeMinInstallments.getOrDefault(type, null);

            List<String> frequencies = typeFrequencyMap.getOrDefault(type, Collections.singletonList(null));

            for (String freq : frequencies) {
                SystemTransactionRule.Frequency frequency = SystemTransactionRule.parseFrequency(freq);
                if(frequency != null){
                    SystemTransactionRule rule = SystemTransactionRule.builder()
                            .type(SystemTransactionRule.TransactionType.valueOf(type))
                            .frequency(frequency)
                            .dayOrDates(typeFrequencyDatesMap.getOrDefault(type, Collections.emptyMap())
                                    .getOrDefault(freq, new ArrayList<>()))
                            .minAmount(minAmount)
                            .minInstallments(minInst)
                            .multiplier(multiplier)
                            .schemeTransaction(schemeTransactions)
                            .build();
                    rules.add(rule);
                }
            }
        }

        return rules;
    }

    public static LocalDate parseToLocalDate(String dateStr) {
        DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        if (dateStr == null || "N/A".equals(dateStr.trim()) || "NA".equals(dateStr.trim()) || dateStr.isBlank()) {
            return null;
        }
        dateStr = dateStr.trim().replace("-", " ");
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }

    @Transactional
    public Optional<SchemeDetails> getSchemeDetailsBySchemeId(long schemeId){
        return amfiSchemeDetailsRepo.findBySchemeId(schemeId);
    }

}
