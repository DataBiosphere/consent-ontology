package org.broadinstitute.dsde.consent.ontology.datause.services;

import java.io.IOException;

public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE }

    String translateDataset(String dataUse) throws IOException;

    String translatePurpose(String dataUse) throws IOException;

}
