package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.ImplementedBy;

import java.io.IOException;

@ImplementedBy(TextTranslationServiceImpl.class)
public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE }

    String translateDatasetDataUse(String dataUse) throws IOException;

    String translatePurposeDataUse(String dataUse) throws IOException;

}
