package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final Logger log = Utils.getLogger(this.getClass());

    private AutocompleteService autocompleteService;

    public DataUseMatcher() { }

    @Inject
    public void setAutocompleteService(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    // Matching Algorithm
    public ImmutablePair<Boolean, List<String>> matchPurposeAndDatasetV2(DataUse purpose, DataUse dataset) throws IOException {
        Map<String, List<String>> purposeDiseaseIdMap = purposeDiseaseIdMap(purpose.getDiseaseRestrictions());

        ImmutablePair<Boolean, List<String>> diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);
        ImmutablePair<Boolean, List<String>> hmbMatch = matchHMB(purpose, dataset, diseaseMatch.getLeft());
        ImmutablePair<Boolean, List<String>> nmdsMatch = matchNMDS(purpose, dataset, diseaseMatch.getLeft());
        ImmutablePair<Boolean, List<String>> controlMatch = matchControlSet(purpose, dataset, diseaseMatch.getLeft());
        ImmutablePair<Boolean, List<String>> nagrMatch = matchNAGR(purpose, dataset);
        ImmutablePair<Boolean, List<String>> poaMatch = matchPOA(purpose, dataset);
        ImmutablePair<Boolean, List<String>> commercialMatch = matchCommercial(purpose, dataset);
        ImmutablePair<Boolean, List<String>> pediatricMatch = matchRSPD(purpose, dataset);
        ImmutablePair<Boolean, List<String>> genderMatch = matchRSG(purpose, dataset);

        log.debug("hmbMatch: " + hmbMatch.getLeft());
        log.debug("diseaseMatch: " + diseaseMatch.getLeft());
        log.debug("nmdsMatch: " + nmdsMatch.getLeft());
        log.debug("controlMatch: " + controlMatch.getLeft());
        log.debug("nagrMatch: " + nagrMatch.getLeft());
        log.debug("poaMatch: " + poaMatch.getLeft());
        log.debug("commercialMatch: " + commercialMatch.getLeft());
        log.debug("pediatricMatch: " + pediatricMatch.getLeft());
        log.debug("genderMatch: " + genderMatch.getLeft());

        Boolean match = hmbMatch.getLeft() &&
                diseaseMatch.getLeft() &&
                nmdsMatch.getLeft() &&
                controlMatch.getLeft() &&
                nagrMatch.getLeft() &&
                poaMatch.getLeft() &&
                commercialMatch.getLeft() &&
                pediatricMatch.getLeft() &&
                genderMatch.getLeft();

        List<String> reasons = Stream.of(
                diseaseMatch.getRight(),
                hmbMatch.getRight(),
                nmdsMatch.getRight(),
                controlMatch.getRight(),
                nagrMatch.getRight(),
                poaMatch.getRight(),
                commercialMatch.getRight(),
                pediatricMatch.getRight(),
                genderMatch.getRight())
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

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
