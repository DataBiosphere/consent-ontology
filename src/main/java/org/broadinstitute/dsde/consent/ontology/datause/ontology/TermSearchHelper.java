package org.broadinstitute.dsde.consent.ontology.datause.ontology;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;

import java.util.List;

public class TermSearchHelper {

    private LuceneOntologyTermSearchAPI lotsa; // = OntologyProvider.getLuceneOntologyTermSearchAPI();

    public List<OntologyTerm> searchSimilarTerms(String search, Integer limit) throws Exception {
        return lotsa.searchSimilarTerms(search, limit);
    }

    @Inject
    public void setLotsa(LuceneOntologyTermSearchAPI lotsa) {
        this.lotsa = lotsa;
    }
}
