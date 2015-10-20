package org.broadinstitute.dsde.consent.ontology.datause.ontology;

import com.google.inject.ImplementedBy;
import java.io.IOException;

@ImplementedBy(TranslationHelperImpl.class)
public interface TranslationHelper {
    String translateSample(String restrictionStr) throws IOException;

    String translatePurpose(String restrictionStr) throws IOException;
}
