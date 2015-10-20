package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.google.inject.ImplementedBy;
import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;

import java.io.IOException;
import java.util.List;

@ImplementedBy(LuceneOntologyTermSearchAPI.class)
public interface OntologyTermSearchAPI {
    List<OntologyTerm> searchSimilarTerms(String search, int limit) throws IOException;

    OntologyTerm findById(String id) throws IOException;
}
