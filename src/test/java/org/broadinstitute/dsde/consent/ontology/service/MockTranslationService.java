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
    public String translateParagraph(String paragraph) {
        return "{\"http://purl.obolibrary.org/obo/DUO_0000007\":{\"title\":\"Disease Specific Research\",\"category\":\"Data Use Permission\"}}";
    }

    @Override
    public String translatePurpose(String dataUse) {
        return "translated purpose";
    }

}
