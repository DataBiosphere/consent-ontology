package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchCommercial;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNAGR;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchPOA;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchRSG;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchRSPD;

public class DataUseMatcher {

    private AutocompleteService autocompleteService;

    public DataUseMatcher() {
    }

    @Inject
    public void setAutocompleteService(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    // Matching Algorithm
    public ImmutablePair<Boolean, List<String>> matchPurposeAndDatasetV2(DataUse purpose, DataUse dataset) throws IOException {
        Map<String, List<String>> purposeDiseaseIdMap = purposeDiseaseIdMap(purpose.getDiseaseRestrictions());
        ImmutablePair<Boolean, List<String>> diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);

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

    // Helper methods

    // Get a map of disease term to list of parent term ids (which also includes disease term id)
    private Map<String, List<String>> purposeDiseaseIdMap(List<String> diseaseRestrictions) throws IOException {
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
                .filter(t -> t.getParents() != null && !t.getParents().isEmpty())
                .flatMap(t -> t.parents.stream())
                .map(p -> p.id)
                .collect(Collectors.toList());
        purposeTermIdList.add(purposeDiseaseId);
        return purposeTermIdList;
    }

}
