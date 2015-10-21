package org.broadinstitute.dsde.consent.ontology;

import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.datause.ontology.TranslationHelper;

public class MockTranslationHelper implements TranslationHelper {

    @Override
    public String translateSample(String restrictionStr) throws IOException {
        return "translated sampleset";  
    }

    @Override
    public String translatePurpose(String restrictionStr) throws IOException {
        return "translated purpose";  
    }
}
