package org.broadinstitute.dsde.consent.ontology.datause.api;

import org.broadinstitute.dsde.consent.ontology.datause.models.OntologyTerm;

import java.io.IOException;
import java.util.List;

public interface OntologyTermSearchAPI {
    List<OntologyTerm> searchSimilarTerms(String search, int limit) throws IOException;

    OntologyTerm findById(String id) throws IOException;
}
