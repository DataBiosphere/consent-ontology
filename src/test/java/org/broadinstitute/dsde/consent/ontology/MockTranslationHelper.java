package org.broadinstitute.dsde.consent.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;

public class MockTranslationHelper implements TextTranslationService {

    @Override
    public String translateSample(String restrictionStr) {
        return "translated sampleset";  
    }

    @Override
    public String translatePurpose(String restrictionStr) {
        return "translated purpose";  
    }

}
