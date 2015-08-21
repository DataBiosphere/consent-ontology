package org.broadinstitute.dsde.consent.ontology.datause.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationResource;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

public class TranslationHelper {

    // ontology translators for consents/purpose
    private TextTranslationResource translator;

    public TranslationHelper() throws IOException, OWLOntologyCreationException {
        translator = new TextTranslationResource(TermSearchHelper.lotsa);
    }

    public String translateSample(String restrictionStr) throws IOException {
        return translator.translateSample(restrictionStr);
    }

    public String translatePurpose(String restrictionStr) throws IOException {
        return translator.translatePurpose(restrictionStr);
    }

}
