package org.broadinstitute.dsde.consent.ontology.datause.services;

import java.util.Map;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;

import java.io.IOException;
import java.util.HashMap;

public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE, PARAGRAPH }

    DataUseSummary translateDataUseSummary(String dataUseString);

    String translateDataset(String dataUse) throws IOException;

    Map<String, Recommendation> translateParagraph(String paragraph) throws IOException;

    String translatePurpose(String dataUse) throws IOException;

}
