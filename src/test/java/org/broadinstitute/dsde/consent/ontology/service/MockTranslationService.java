package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;

public class MockTranslationService implements TextTranslationService {

    @Override
    public DataUseSummary translateDataUseSummary(String dataUseString) {
        return new DataUseSummary();
    }

    @Override
    public String translateDataset(String dataUse) {
        return "translated dataset";
    }

    @Override
    public String translatePurpose(String dataUse) {
        return "translated purpose";
    }

}
