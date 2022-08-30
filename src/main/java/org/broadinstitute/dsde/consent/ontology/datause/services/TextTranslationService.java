package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.broadinstitute.dsde.consent.ontology.model.DataUseRecommendation;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;

import java.io.IOException;
import java.util.HashMap;

public interface TextTranslationService {

    enum TranslateFor { DATASET, PURPOSE, PARAGRAPH }

    DataUseSummary translateDataUseSummary(String dataUseString);

    String translateDataset(String dataUse) throws IOException;

    DataUseRecommendation translateParagraph(String paragraph) throws Exception;

    String translatePurpose(String dataUse) throws IOException;

}
