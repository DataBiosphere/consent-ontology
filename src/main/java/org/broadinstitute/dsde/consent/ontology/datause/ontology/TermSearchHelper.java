package org.broadinstitute.dsde.consent.ontology.datause.ontology;

import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.services.OntologyProvider;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;

import java.util.List;

public class TermSearchHelper {

    public static final LuceneOntologyTermSearchAPI lotsa = OntologyProvider.getLuceneOntologyTermSearchAPI();

    public List<OntologyTerm> searchSimilarTerms(String search, Integer limit) throws Exception {
        return lotsa.searchSimilarTerms(search, limit);
    }

}
