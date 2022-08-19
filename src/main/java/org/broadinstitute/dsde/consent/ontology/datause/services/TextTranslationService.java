package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;

import java.io.IOException;

public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE, PARAGRAPH }

    DataUseSummary translateDataUseSummary(String dataUseString);

    String translateDataset(String dataUse) throws IOException;

    String translateParagraph(String paragraph) throws IOException;

    String translatePurpose(String dataUse) throws IOException;

}
