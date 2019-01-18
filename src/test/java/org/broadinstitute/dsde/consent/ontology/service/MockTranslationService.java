package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;

public class MockTranslationService implements TextTranslationService {

    @Override
    public String translateDataset(String dataUse) {
        return "translated dataset";
    }

    @Override
    public String translatePurpose(String dataUse) {
        return "translated purpose";
    }

}
