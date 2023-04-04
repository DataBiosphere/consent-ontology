package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchPOA;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.matchCommercial;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCasesV3.abstainDecision;

public class DataUseMatcherV3 {

  private AutocompleteService autocompleteService;

  public DataUseMatcherV3() {
  }

  @Inject
  public void setAutocompleteService(AutocompleteService autocompleteService) {
    this.autocompleteService = autocompleteService;
  }

  // Matching Algorithm
  public MatchResult matchPurposeAndDatasetV3(DataUseV3 purpose, DataUseV3 dataset) {
    Map<String, List<String>> purposeDiseaseIdMap;
    try {
      purposeDiseaseIdMap = generatePurposeDiseaseIdMap(purpose.getDiseaseRestrictions());
    } catch (Exception e) {
      String purposeRestrictions = StringUtils.join(purpose.getDiseaseRestrictions(), ", ");
      List<String> errors = Arrays.asList(e.getMessage(), "Error found in one of the purpose terms: " + purposeRestrictions);
      return MatchResult.from(MatchResultType.DENY, errors);
    }

    MatchResult diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);
    final List<MatchResult> matchReasons = new ArrayList<>();
    matchReasons.add(diseaseMatch);
    matchReasons.add(matchDiseases(purpose, dataset, purposeDiseaseIdMap));
    matchReasons.add(matchHMB(purpose, dataset));
    matchReasons.add(matchPOA(purpose, dataset));
    matchReasons.add(matchMDS(purpose, dataset, diseaseMatch.getLeft()));
    matchReasons.add(matchCommercial(purpose, dataset));
    matchReasons.add(abstainDecision(purpose, dataset, purposeDiseaseIdMap, diseaseMatch.getLeft()));
    final boolean match = matchReasons.stream().
        map(MatchResult::getLeft).
        allMatch(MatchResultType.isApprove());
    final List<String> reasons = matchReasons.stream().
        map(MatchResult::getRight).
        flatMap(Collection::stream).
        filter(StringUtils::isNotBlank).
        collect(Collectors.toList());
    return MatchResult.from(match, reasons);
  }

  // Helper methods

  // Get a map of disease term to list of parent term ids (which also includes disease term id)
  private Map<String, List<String>> generatePurposeDiseaseIdMap(List<String> diseaseRestrictions) throws IOException {
    Map<String, List<String>> map = new HashMap<>();
    for (String r : diseaseRestrictions) {
      map.put(r, getParentTermIds(r));
    }
    return map;
  }

  // Get a list of term ids that represent a disease term + all parent ids
  private List<String> getParentTermIds(String purposeDiseaseId) throws IOException {
    List<String> purposeTermIdList = autocompleteService.lookupById(purposeDiseaseId)
        .stream()
        .filter(Objects::nonNull)
        .filter(t -> Objects.nonNull(t.getParents()) && !t.getParents().isEmpty())
        .flatMap(t -> t.parents.stream())
        .map(p -> p.id)
        .collect(Collectors.toList());
    purposeTermIdList.add(purposeDiseaseId);
    return purposeTermIdList;
  }

}
