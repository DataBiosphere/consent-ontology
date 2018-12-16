package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.ImplementedBy;

import java.io.IOException;

@ImplementedBy(TextTranslationServiceImpl.class)
public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE }

    String translateDataset(String dataUse) throws IOException;

    String translatePurpose(String dataUse) throws IOException;

}
