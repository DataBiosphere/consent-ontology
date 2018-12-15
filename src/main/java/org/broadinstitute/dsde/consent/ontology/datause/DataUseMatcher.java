package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNAGR;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchPOA;

public class DataUseMatcher {

    private final Logger log = LoggerFactory.getLogger(DataUseMatcher.class);

    private AutocompleteService autocompleteService;

    public DataUseMatcher() { }

    @Inject
    public void setAutocompleteService(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    // Matching Algorithm
    public boolean matchPurposeAndDataset(DataUse purpose, DataUse dataset) throws IOException {
        Map<String, List<String>> purposeDiseaseIdMap = purposeDiseaseIdMap(purpose.getDiseaseRestrictions());

        boolean diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);
        boolean hmbMatch = matchHMB(purpose, dataset, diseaseMatch);
        boolean nmdsMatch = matchNMDS(purpose, dataset, diseaseMatch);
        boolean controlMatch = matchControlSet(purpose, dataset, diseaseMatch);
        boolean nagrMatch = matchNAGR(purpose, dataset);
        boolean poaMatch = matchPOA(purpose, dataset);

        log.debug("hmbMatch: " + hmbMatch);
        log.debug("diseaseMatch: " + diseaseMatch);
        log.debug("nmdsMatch: " + nmdsMatch);
        log.debug("controlMatch: " + controlMatch);
        log.debug("nagrMatch: " + nagrMatch);
        log.debug("poaMatch: " + poaMatch);

        return hmbMatch &&
                diseaseMatch &&
                nmdsMatch &&
                controlMatch &&
                nagrMatch &&
                poaMatch;
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
                .flatMap(t -> t.parents.stream())
                .map(p -> p.id)
                .collect(Collectors.toList());
        purposeTermIdList.add(purposeDiseaseId);
        return purposeTermIdList;
    }

}
