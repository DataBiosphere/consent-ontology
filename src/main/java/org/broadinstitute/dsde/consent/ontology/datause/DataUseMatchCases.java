package org.broadinstitute.dsde.consent.ontology.datause;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * General case is that we make very granular comparisons.
 * Short-circuit checks if possible. Need to evaluate carefully if short circuiting can happen on
 *      either the dataset, purpose, or both.
 * Go through the result cases individually.
 * Prefer longer, descriptive expressions over terse comparisons. We're not looking for
 * the most concise code possible, but instead favor clarity.
 */
@SuppressWarnings({"RedundantIfStatement", "ConstantConditions"})
class DataUseMatchCases {

    private static final String na = "N/A";
    private static final String male = "Male";
    private static final String female = "Female";

    /**
     * RP: HMB
     * Datasets:
     *      Any dataset tagged with GRU
     *      Any dataset tagged with HMB
     *      *** Adding based on HMB Truth Table use case support ***
     *      TODO: Validate that this is acceptable with Product Owner. See https://broadinstitute.atlassian.net/browse/BTRX-481
     *      Any DS match
     */
    static boolean matchHMB(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit hmb if not set
        if (purpose.getHmbResearch() == null && dataset.getHmbResearch() == null) {
            return true;
        }

        boolean purposeGRU = getNullableOrFalse(purpose.getGeneralUse());
        boolean purposeHMB = getNullableOrFalse(purpose.getHmbResearch());
        boolean datasetGRU = getNullableOrFalse(dataset.getGeneralUse());
        boolean datasetHMB = getNullableOrFalse(dataset.getHmbResearch());

        if (datasetHMB && purposeGRU) {
            return false;
        }

        if (purposeHMB && datasetGRU) {
            return true;
        }
        if (purposeHMB && datasetHMB) {
            return true;
        }

        if (diseaseMatch) {
            return true;
        }

        return false;
    }

    /**
     * RP: Disease Focused Research
     * Future use is limited to research involving the following disease area(s) [DS]
     * Datasets:
     *      Any dataset with GRU=true
     *      Any dataset with HMB=true
     *      Any dataset tagged to this disease exactly
     *      Any dataset tagged to a DOID ontology Parent of disease X
     */
    static boolean matchDiseases(DataUse purpose, DataUse dataset, Map<String, List<String>> purposeDiseaseIdMap) {
        // short-circuit if no disease focused research
        if (purpose.getDiseaseRestrictions().isEmpty() && dataset.getDiseaseRestrictions().isEmpty()) {
            return true;
        }
        if (getNullableOrFalse(dataset.getGeneralUse())) {
            return true;
        }
        if (getNullableOrFalse(dataset.getHmbResearch())) {
            return true;
        } else {
            // We want all purpose disease IDs to be a subclass of any dataset disease ID
            Set<Boolean> matches = purposeDiseaseIdMap
                    .values()
                    .stream()
                    .map(idList -> idList
                            .stream()
                            .anyMatch(dataset.getDiseaseRestrictions()::contains))
                    .collect(Collectors.toSet());
            return !matches.contains(false);
        }
    }

    /**
     * RP: Methods development/Validation study
     * Future use for methods research (analytic/software/technology development) outside the
     *      bounds of the other specified restrictions is prohibited [NMDS]
     * Datasets:
     *      Any dataset where NMDS is false
     *      Any dataset where NMDS is true AND DS-X match
     */
    static boolean matchNMDS(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no nmds clause
        if (purpose.getMethodsResearch() == null && dataset.getMethodsResearch() == null) {
            return true;
        }

        boolean datasetNMDS = getNullableOrFalse(dataset.getMethodsResearch());
        if (!datasetNMDS) {
            return true;
        }

        if (datasetNMDS && diseaseMatch) {
            return true;
        }

        return false;
    }

    /**
     * RP: Control Set
     * Future use as a control set for diseases other than those specified is prohibited [NCTRL]
     * Datasets:
     *      Any dataset where NCTRL is false and is (GRU or HMB)
     *      Any DS-X match, if user specified a disease in the res purpose search
     */
    static boolean matchControlSet(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no control set clause
        if (purpose.getControlSetOption() == null) {
            return true;
        }

        boolean datasetNCTRL = getNullable(purpose.getControlSetOption());
        boolean datasetGRUorHMB = (getNullableOrFalse(dataset.getGeneralUse()) || getNullableOrFalse(dataset.getHmbResearch()));
        if (datasetNCTRL && datasetGRUorHMB) {
            return true;
        }

        if (!purpose.getDiseaseRestrictions().isEmpty() && diseaseMatch) {
            return true;
        }

        return false;
    }

    /**
     * RP: Aggregate analysis to understand variation in the general population
     * Future use of aggregate-level data for general research purposes is prohibited [NAGR] (Yes | No | Unspecified)
     * Datasets:
     *      Any dataset where NAGR is false and is (GRU or HMB)
     */
    static boolean matchNAGR(DataUse purpose, DataUse dataset) {
        // short-circuit if no aggregate clause
        if (purpose.getAggregateResearch() == null && dataset.getAggregateResearch() == null) {
            return true;
        }

        boolean purposeNAGR = getNullable(purpose.getAggregateResearch());
        boolean datasetNAGR = getNullable(dataset.getAggregateResearch());

        return purposeNAGR &&
                !datasetNAGR &&
                (getNullableOrFalse(dataset.getHmbResearch()) || getNullableOrFalse(dataset.getGeneralUse()));
    }

    /**
     * RP: Study population origins or ancestry
     * Future use is limited to research involving a specific population [POA]
     * Datasets:
     *      Any dataset tagged with GRU
     */
    static boolean matchPOA(DataUse purpose, DataUse dataset) {
        // short-circuit if no POA clause
        if (purpose.getPopulationOriginsAncestry() == null) {
            return true;
        }

        return purpose.getPopulationOriginsAncestry() &&
                getNullableOrFalse(dataset.getGeneralUse());
    }

    /**
     * RP: Commercial purpose/by a commercial entity
     * Future commercial use is prohibited [NCU]
     * Future use by for-profit entities is prohibited [NPU]
     * Datasets:
     *      Any dataset where NPU and NCU are both false
     */
    static boolean matchCommercial(DataUse purpose, DataUse dataset) {
        // short-circuit if no commercial clause
        if (purpose.getCommercialUse() == null || dataset.getCommercialUse() == null) {
            return true;
        }

        boolean purposeCommercial = getNullableOrFalse(purpose.getCommercialUse());
        boolean datasetCommercial = getNullableOrFalse(dataset.getCommercialUse());

        if (purposeCommercial) {
            return datasetCommercial;
        }

        if (!purposeCommercial) {
            return !datasetCommercial;
        }

        return false;
    }

    /**
     * TODO: Validate that this is acceptable with Product Owner. See https://broadinstitute.atlassian.net/browse/DUOS-259
     * RP: Restricted to pediatric
     * Future use is limited to pediatric research [RS-PD] (required) (Yes | No)
     * Datasets:
     *      Any dataset tagged with RS-PD
     */
    static boolean matchRSPD(DataUse purpose, DataUse dataset) {
        // short-circuit if dataset is not restricted
        if (dataset.getPediatric() == null || !dataset.getPediatric()) {
            return true;
        }

        // also short circuit if RS is not tagged:
        if (purpose.getPediatric() == null) {
            return true;
        }

        return purpose.getPediatric() && dataset.getPediatric();
    }

    /**
     * TODO: Validate that this is acceptable with Product Owner. See https://broadinstitute.atlassian.net/browse/DUOS-259
     * RP: Restricted to a gender
     * Women
     * Men
     * Other
     * Future use is limited to research involving a particular gender [RS-G] (required) (Male | Female | N/A)
     * Any dataset tagged with:
     *      RS-G:F or N/A
     *      RS-G:M or N/A
     */
    static boolean matchRSG(DataUse purpose, DataUse dataset) {
        // short-circuit if dataset is not restricted
        if (dataset.getGender() == null) {
            return true;
        }

        String purposeGender = Optional.ofNullable(purpose.getGender()).orElse(na);

        if (dataset.getGender().equalsIgnoreCase(na)) {
            return true;
        }

        if (purposeGender.equalsIgnoreCase(male)) {
            return dataset.getGender().equalsIgnoreCase(male);
        }

        if (purposeGender.equalsIgnoreCase(female)) {
            return dataset.getGender().equalsIgnoreCase(female);
        }

        return false;
    }

    // Helper Methods

    /**
     * @param bool nullable boolean value
     * @return boolean The value, or false otherwise
     */
    private static boolean getNullableOrFalse(Boolean bool) {
        return Optional.ofNullable(bool).orElse(false);
    }

    /**
     * @param yesOrNo nullable string value
     * @return boolean True if "yes", false otherwise
     */
    private static boolean getNullable(String yesOrNo) {
        return Optional.ofNullable(yesOrNo).orElse("no").equalsIgnoreCase("yes");
    }

}
