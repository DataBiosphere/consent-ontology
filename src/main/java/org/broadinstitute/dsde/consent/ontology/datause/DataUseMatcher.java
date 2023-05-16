package org.broadinstitute.dsde.consent.ontology.datause;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchCommercial;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNAGR;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchPOA;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchRSG;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchRSPD;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.util.DataUseUtil;

public class DataUseMatcher {

  public DataUseMatcher() {
  }

  private DataUseUtil dataUseUtil = new DataUseUtil();

  @Inject
  public void setAutocompleteService(AutocompleteService autocompleteService) {
    dataUseUtil.setAutocompleteService(autocompleteService);
  }

  // Matching Algorithm
  public ImmutablePair<Boolean, List<String>> matchPurposeAndDatasetV2(DataUse purpose,
      DataUse dataset) {
    Map<String, List<String>> purposeDiseaseIdMap;
    try {
      purposeDiseaseIdMap = dataUseUtil.generatePurposeDiseaseIdMap(
          purpose.getDiseaseRestrictions());
    } catch (Exception e) {
      String purposeRestrictions = StringUtils.join(purpose.getDiseaseRestrictions(), ", ");
      List<String> errors = Arrays.asList(e.getMessage(),
          "Error found in one of the purpose terms: " + purposeRestrictions);
      return ImmutablePair.of(false, errors);
    }

    ImmutablePair<Boolean, List<String>> diseaseMatch = matchDiseases(purpose, dataset,
        purposeDiseaseIdMap);
    final List<ImmutablePair<Boolean, List<String>>> matchReasons = new ArrayList<>();
    matchReasons.add(matchDiseases(purpose, dataset, purposeDiseaseIdMap));
    matchReasons.add(matchHMB(purpose, dataset, diseaseMatch.getLeft()));
    matchReasons.add(matchNMDS(purpose, dataset, diseaseMatch.getLeft()));
    matchReasons.add(matchControlSet(purpose, dataset, diseaseMatch.getLeft()));
    matchReasons.add(matchNAGR(purpose, dataset));
    matchReasons.add(matchPOA(purpose, dataset));
    matchReasons.add(matchCommercial(purpose, dataset));
    matchReasons.add(matchRSPD(purpose, dataset));
    matchReasons.add(matchRSG(purpose, dataset));
    final Boolean match = matchReasons.stream().
        map(ImmutablePair::getLeft).
        allMatch(BooleanUtils::isTrue);
    final List<String> reasons = matchReasons.stream().
        map(ImmutablePair::getRight).
        flatMap(Collection::stream).
        filter(StringUtils::isNotBlank).
        collect(Collectors.toList());
    return ImmutablePair.of(match, reasons);
  }
}
