package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.util.DataUseUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchPOA;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchCommercial;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.abstainDecision;

public class DataUseMatcherV3 {
  public DataUseMatcherV3() {}
  private DataUseUtil dataUseUtil = new DataUseUtil();
  @Inject
  public void setAutocompleteService(AutocompleteService autocompleteService) {
    dataUseUtil.setAutocompleteService(autocompleteService);
  }

  // Matching Algorithm
  public MatchResult matchPurposeAndDatasetV3(DataUseV3 purpose, DataUseV3 dataset) {
    Map<String, List<String>> purposeDiseaseIdMap;
    try {
      purposeDiseaseIdMap = dataUseUtil.generatePurposeDiseaseIdMap(purpose.getDiseaseRestrictions());
    } catch (Exception e) {
      String purposeRestrictions = StringUtils.join(purpose.getDiseaseRestrictions(), ", ");
      List<String> errors = Arrays.asList(e.getMessage(), "Error found in one of the purpose terms: " + purposeRestrictions);
      return MatchResult.from(MatchResultType.DENY, errors);
    }

    MatchResult diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);
    final List<MatchResult> matchReasons = new ArrayList<>();
    matchReasons.add(diseaseMatch);
    matchReasons.add(matchHMB(purpose, dataset));
    matchReasons.add(matchPOA(purpose, dataset));
    matchReasons.add(matchMDS(purpose, dataset, diseaseMatch.getMatchResultType()));
    matchReasons.add(matchCommercial(purpose, dataset));
    matchReasons.add(abstainDecision(purpose, dataset, purposeDiseaseIdMap, diseaseMatch.getMatchResultType()));
    final boolean allMatch = matchReasons.stream().
        map(MatchResult::getMatchResultType).
        allMatch(rt -> rt.equals(MatchResultType.APPROVE));
    final boolean anyMatch = matchReasons.stream().
        map(MatchResult::getMatchResultType).
        anyMatch(rt -> rt.equals(MatchResultType.ABSTAIN));
    final List<String> reasons = matchReasons.stream().
        map(MatchResult::getMessage).
        flatMap(Collection::stream).
        filter(StringUtils::isNotBlank).
        collect(Collectors.toList());
    // if all items match, decision is APPROVED
    if (allMatch) {
      return MatchResult.from(MatchResultType.APPROVE, reasons);
    }
    // if not, determine whether DENY or ABSTAIN
    if (anyMatch){
      return MatchResult.from(MatchResultType.ABSTAIN, reasons);
    }
    return MatchResult.from(MatchResultType.DENY, reasons);
  }
}
