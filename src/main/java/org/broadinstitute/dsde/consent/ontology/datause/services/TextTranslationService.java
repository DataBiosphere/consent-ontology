package org.broadinstitute.dsde.consent.ontology.datause.services;

import java.io.IOException;
import java.util.Map;
import org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;

public interface TextTranslationService {

  DataUseSummary translateDataUseSummary(String dataUseString);

  String translateDataset(String dataUse) throws IOException;

  Map<String, Recommendation> translateParagraph(String paragraph) throws IOException;

  String translatePurpose(String dataUse) throws IOException;

  String translate(DataUse dataUse, TranslateFor translateFor);
}
