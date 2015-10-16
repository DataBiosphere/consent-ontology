package org.broadinstitute.dsde.consent.ontology.datause.ontology;

import com.google.inject.Inject;

import java.io.IOException;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;

public class TranslationHelperImpl implements TranslationHelper {

    // ontology translators for consents/purpose
    private TextTranslationService translator;
    
    public TranslationHelperImpl() {
    }

    @Override
    public String translateSample(String restrictionStr) throws IOException {
        return translator.translateSample(restrictionStr);
    }

    @Override
    public String translatePurpose(String restrictionStr) throws IOException {
        return translator.translatePurpose(restrictionStr);
    }

    @Inject
    public void setTranslator(TextTranslationService translator) {
        this.translator = translator;
    }

}
