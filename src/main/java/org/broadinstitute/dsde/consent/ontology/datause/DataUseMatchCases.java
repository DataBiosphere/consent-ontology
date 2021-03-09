package org.broadinstitute.dsde.consent.ontology.datause;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * General case is that we make very granular comparisons.
 * Short-circuit checks if possible. Need to evaluate carefully if short circuiting can happen on
 *      either the dataset, purpose, or both.
 * Go through the result cases individually.
 * Prefer longer, descriptive expressions over terse comparisons. We're not looking for
 * the most concise code possible, but instead favor clarity.
 */
@SuppressWarnings({"ConstantConditions"})
class DataUseMatchCases {

    private static final String na = "N/A";
    private static final String male = "Male";
    private static final String female = "Female";
    private static final String HMB_F1 = "The GRU Research Purpose does not match the HMB data use limitations.";
    private static final String HMB_F2 = "The HMB Research Purpose does not match the Disease-Specific data use limitations.";
    private static final String DS_F1 = "The unspecified Disease Research Purpose does not match the Disease-Specific data use limitations.";
    private static final String DS_F2 = "The Disease-Specific: %s Research Purpose is not a valid subclass of the Disease-Specific data use limitations.";
    private static final String NMDS_F1 = "The Methods development Research Purpose is prohibited by the NMDS No Methods Development data use limitation.";
    private static final String NCTRL_F1 = "The Controls Use Research Purpose is prohibited by the NCTRL No Controls Use data use limitation.";
    private static final String NCTRL_F2 = "TThe Disease-Specific: %s Research Purpose is not a valid subclass of the Disease-Specific data use limitations.";
    private static final String NAGR_F1 = "The Aggregate-level Research Purpose is prohibited by the NAGR No Aggregate-level data use limitation.";
    private static final String POA_F1 = "The Populations, Origins, Ancestry Research Purpose does not match the HMB or Disease-Specific data use limitation.";
    private static final String NCU_F1 = "The Commercial Use Research Purpose does not match the No Commerical Use data use limitation.";
    private static final String RSPD_F1 = "The Research Purpose did not specify Population: Pediatric as specified by the data use limitations.";
    private static final String RSG_F1 = "The Research Purpose did not specify Population: Female as specified by the data use limitations.";
    private static final String RSG_F2 = "The Research Purpose did not specify Population: Male as specified by the data use limitations.";

    /**
     * RP: HMB
     * Datasets:
     *      Any dataset tagged with GRU
     *      Any dataset tagged with HMB
     *      Any DS match
     *
     * @param purpose The data use object representing the Research Purpose
     * @param dataset The data use object representing the Dataset
     * @param diseaseMatch Indicator for a pre-existing disease match
     */
    static ImmutablePair<Boolean, List<String>> matchHMB(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit hmb if not set
        if (purpose.getHmbResearch() == null && dataset.getHmbResearch() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        boolean purposeGRU = getNullableOrFalse(purpose.getGeneralUse());
        boolean purposeHMB = getNullableOrFalse(purpose.getHmbResearch());
        boolean datasetGRU = getNullableOrFalse(dataset.getGeneralUse());
        boolean datasetHMB = getNullableOrFalse(dataset.getHmbResearch());

        if (datasetHMB && purposeGRU) {
            return ImmutablePair.of(false, Collections.singletonList(HMB_F1));
        }

        if (purposeHMB && datasetGRU) {
            return ImmutablePair.of(true, Collections.emptyList());
        }
        if (purposeHMB && datasetHMB) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        if (diseaseMatch) {
            return ImmutablePair.of(true, Collections.emptyList());
        } else {
            return ImmutablePair.of(false, Collections.singletonList(HMB_F2));
        }

    }

    /**
     * RP: Disease Focused Research
     * Future use is limited to research involving the following disease area(s) [DS]
     * Datasets:
     *      Any dataset with GRU=true
     *      Any dataset with HMB=true
     *      Any dataset tagged to this disease exactly
     *      Any dataset tagged to a DOID ontology Parent of disease X
     *
     * @param purpose The data use object representing the Research Purpose
     * @param dataset The data use object representing the Dataset
     * @param purposeDiseaseIdMap is a map of each purpose term id to a list of that term's parent term ids
     */
    static ImmutablePair<Boolean, List<String>> matchDiseases(DataUse purpose, DataUse dataset, Map<String, List<String>> purposeDiseaseIdMap) {
        // short-circuit if no disease focused research
        if (purpose.getDiseaseRestrictions().isEmpty() && dataset.getDiseaseRestrictions().isEmpty()) {
            return ImmutablePair.of(true, Collections.emptyList());
        }
        // short-circuit if dataset is GRU
        if (getNullableOrFalse(dataset.getGeneralUse())) {
            return ImmutablePair.of(true, Collections.emptyList());
        }
        // short-circuit if dataset is HMB
        if (getNullableOrFalse(dataset.getHmbResearch())) {
            return ImmutablePair.of(true, Collections.emptyList());
        }
        // short-circuit if no diseases specified in the purpose, but the dataset specifies diseases
        if (!dataset.getDiseaseRestrictions().isEmpty() && purpose.getDiseaseRestrictions().isEmpty()) {
            return ImmutablePair.of(false, Collections.singletonList(DS_F1));
        }

        // We want all purpose disease IDs to be a subclass of any dataset disease ID
        List<String> failures = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : purposeDiseaseIdMap.entrySet()) {
            boolean match = entry.getValue()
                    .stream()
                    .anyMatch(dataset.getDiseaseRestrictions()::contains);
            if (!match) {
                failures.add(String.format(DS_F2,  entry.getKey()));
            }
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
    }

    /**
     * RP: Methods development/Validation study
     * Future use for methods research (analytic/software/technology development) outside the
     *      bounds of the other specified restrictions is prohibited [NMDS]
     * Datasets:
     *      Any dataset where NMDS is false
     *      Any dataset where NMDS is true AND DS-X match
     */
    static ImmutablePair<Boolean, List<String>> matchNMDS(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no nmds clause
        if (purpose.getMethodsResearch() == null && dataset.getMethodsResearch() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        boolean datasetNMDS = getNullableOrFalse(dataset.getMethodsResearch());
        if (!datasetNMDS) {
            return ImmutablePair.of(true, Collections.emptyList());
        } else {
            failures.add(NMDS_F1);
        }

        if (datasetNMDS && diseaseMatch) {
            return ImmutablePair.of(true, Collections.emptyList());
        } else {
            // If there is a disease failure, that will already be captured in the disease match block
            return ImmutablePair.of(false, failures);
        }
    }

    /**
     * RP: Control Set
     * Future use as a control set for diseases other than those specified is prohibited [NCTRL]
     * Datasets:
     *      Any dataset where NCTRL is false and is (GRU or HMB)
     *      Any DS-X match, if user specified a disease in the res purpose search
     */
    static ImmutablePair<Boolean, List<String>> matchControlSet(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no control set clause
        if (purpose.getControlSetOption() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        boolean datasetNCTRL = getNullable(purpose.getControlSetOption());
        boolean datasetGRUorHMB = (getNullableOrFalse(dataset.getGeneralUse()) || getNullableOrFalse(dataset.getHmbResearch()));
        if (datasetNCTRL && datasetGRUorHMB) {
            return ImmutablePair.of(true, Collections.emptyList());
        } else {
            failures.add(NCTRL_F1);
        }

        if (!purpose.getDiseaseRestrictions().isEmpty() && diseaseMatch) {
            return ImmutablePair.of(true, Collections.emptyList());
        } else {
            // If there is a disease failure, that will already be captured in the disease match block
            return ImmutablePair.of(false, failures);
        }
    }

    /**
     * RP: Aggregate analysis to understand variation in the general population
     * Future use of aggregate-level data for general research purposes is prohibited [NAGR] (Yes | No | Unspecified)
     * Datasets:
     *      Any dataset where NAGR is false and is (GRU or HMB)
     */
    static ImmutablePair<Boolean, List<String>> matchNAGR(DataUse purpose, DataUse dataset) {
        // short-circuit if no aggregate clause
        if (purpose.getAggregateResearch() == null && dataset.getAggregateResearch() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        boolean purposeNAGR = getNullable(purpose.getAggregateResearch());
        boolean datasetNAGR = getNullable(dataset.getAggregateResearch());

        if (!(purposeNAGR &&
                !datasetNAGR &&
                (getNullableOrFalse(dataset.getHmbResearch()) || getNullableOrFalse(dataset.getGeneralUse())))) {
            failures.add(NAGR_F1);
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
    }

    /**
     * RP: Study population origins or ancestry
     * Future use is limited to research involving a specific population [POA]
     * Datasets:
     *      Any dataset tagged with GRU
     */
    static ImmutablePair<Boolean, List<String>> matchPOA(DataUse purpose, DataUse dataset) {
        // short-circuit if no POA clause
        if (purpose.getPopulationOriginsAncestry() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        if (!(purpose.getPopulationOriginsAncestry() &&
                getNullableOrFalse(dataset.getGeneralUse()))) {
            failures.add(POA_F1);
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
    }

    /**
     * RP: Commercial purpose/by a commercial entity
     * Future commercial use is prohibited [NCU]
     * Future use by for-profit entities is prohibited [NPU]
     * Datasets:
     *      Any dataset where NPU and NCU are both false
     */
    static ImmutablePair<Boolean, List<String>> matchCommercial(DataUse purpose, DataUse dataset) {
        // short-circuit if no commercial clause
        if (purpose.getCommercialUse() == null || dataset.getCommercialUse() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        boolean purposeCommercial = getNullableOrFalse(purpose.getCommercialUse());
        boolean datasetCommercial = getNullableOrFalse(dataset.getCommercialUse());

        if (purposeCommercial && !datasetCommercial) {
            failures.add(NCU_F1);
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
    }

    /**
     * RP: Restricted to pediatric
     * Future use is limited to pediatric research [RS-PD] (required) (Yes | No)
     * Datasets:
     *      Any dataset tagged with RS-PD
     */
    static ImmutablePair<Boolean, List<String>> matchRSPD(DataUse purpose, DataUse dataset) {
        // short-circuit if dataset is not restricted
        if (dataset.getPediatric() == null || !dataset.getPediatric()) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        // also short circuit if RS is not tagged:
        if (purpose.getPediatric() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        if (!(purpose.getPediatric() && dataset.getPediatric())) {
            failures.add(RSPD_F1);
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
    }

    /**
     * RP: Restricted to a gender
     * Women
     * Men
     * Other
     * Future use is limited to research involving a particular gender [RS-G] (required) (Male | Female | N/A)
     * Any dataset tagged with:
     *      RS-G:F or N/A
     *      RS-G:M or N/A
     */
    static ImmutablePair<Boolean, List<String>> matchRSG(DataUse purpose, DataUse dataset) {
        // short-circuit if dataset is not restricted
        if (dataset.getGender() == null) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        String purposeGender = Optional.ofNullable(purpose.getGender()).orElse(na);

        if (dataset.getGender().equalsIgnoreCase(na)) {
            return ImmutablePair.of(true, Collections.emptyList());
        }

        List<String> failures = new ArrayList<>();
        if (purposeGender.equalsIgnoreCase(male)) {
            if (!dataset.getGender().equalsIgnoreCase(male)) {
                failures.add(RSG_F1);
            }
        }

        if (purposeGender.equalsIgnoreCase(female)) {
            if (!dataset.getGender().equalsIgnoreCase(female)) {
                failures.add(RSG_F2);
            }
        }

        return ImmutablePair.of(failures.isEmpty(), failures);
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
