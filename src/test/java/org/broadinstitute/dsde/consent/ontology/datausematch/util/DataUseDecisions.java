package org.broadinstitute.dsde.consent.ontology.datausematch.util;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.util.Optional;

public class DataUseDecisions {

    private static final Logger log = Logger.getLogger(DataUseDecisions.class);

    /**
     * RP: Methods development/Validation study
     * Datasets:
     *      Any dataset where NMDS is false
     *      Any dataset where NMDS is true AND DS-X match
     */
    public static boolean matchNMDS(DataUse purpose, DataUse dataset, boolean diseaseMatch) {
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
