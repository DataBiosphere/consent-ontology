package org.broadinstitute.dsde.consent.ontology.datausematch.util;

import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.Optional;

/**
 * General case is that we make very granular comparisons.
 * Short-circuit checks if possible.
 * Go through the result cases one by one.
 * Prefer longer, descriptive expressions over terse comparisons. We're not looking for
 * the most concise code possible, but instead it is critical that we favor clarity first.
 */
@SuppressWarnings("RedundantIfStatement")
public class DataUseDecisions {

    /**
     * RP: HMB
     * Datasets:
     *      Any dataset tagged with GRU
     *      Any dataset tagged with HMB
     */
    public static boolean matchHMB(DataUse purpose, DataUse dataset) {
        // short-circuit hmb if not set
        if (purpose.getHmbResearch() == null && dataset.getHmbResearch() == null) {
            return true;
        }

        boolean purposeHMB = getNullable(purpose.getHmbResearch());
        boolean datasetGRU = getNullable(dataset.getGeneralUse());
        boolean datasetHMB = getNullable(dataset.getHmbResearch());

        if (purposeHMB && datasetGRU) {
            return true;
        }
        if (purposeHMB && datasetHMB) {
            return true;
        }
        return false;
    }

    /**
     * RP: Methods development/Validation study
     * Datasets:
     *      Any dataset where NMDS is false
     *      Any dataset where NMDS is true AND DS-X match
     */
    public static boolean matchNMDS(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no nmds clause
        if (purpose.getMethodsResearch() == null) {
            return true;
        }

        boolean datasetNMDS = getNullable(dataset.getMethodsResearch());
        boolean purposeDiseaseMatch = !purpose.getDiseaseRestrictions().isEmpty() && diseaseMatch;
        return !datasetNMDS || purposeDiseaseMatch;
    }

    /**
     * RP: Control Set
     * Datasets:
     *      Any dataset where NCTRL is false and is (GRU or HMB)
     *      Any DS-X match, if user specified a disease in the res purpose search
     */
    public static boolean matchControlSet(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
        // short-circuit if no control set clause
        if (purpose.getControlSetOption() == null) {
            return true;
        }

        boolean datasetNCTRL = getNullable(purpose.getControlSetOption());
        boolean datasetGRUorHMB = (getNullable(dataset.getGeneralUse()) || getNullable(dataset.getHmbResearch()));
        if (datasetNCTRL && datasetGRUorHMB) {
            return true;
        }

        if (!purpose.getDiseaseRestrictions().isEmpty() && diseaseMatch) {
            return true;
        }

        return false;
    }

    public static boolean getNullable(Boolean bool) {
        return Optional.ofNullable(bool).orElse(false);
    }

    private static boolean getNullable(String yesOrNo) {
        return Optional.ofNullable(yesOrNo).orElse("no").equalsIgnoreCase("yes");
    }

}
