package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.apache.lucene.store.RAMDirectory;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ClassLoaderResourceLoader;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

public class OntologyProvider {
    private static LuceneOntologyTermSearchAPI lotsa = null;

    public static synchronized LuceneOntologyTermSearchAPI getLuceneOntologyTermSearchAPI() {
        int count = 3;
        // retry a few times if it fails...
        while (lotsa == null && count-- > 0) {
            try {
                Collection<String> resources = new OntologyList().getResources();
                Iterator<String> resourceIter = resources.iterator();
                InputStream[] ontologies = new InputStream[resources.size()];
                for (int i = 0; i < resources.size() && resourceIter.hasNext(); i++) {
                    ontologies[i] = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceIter.next());
                }
                lotsa = new LuceneOntologyTermSearchAPI(new RAMDirectory(), ontologies);
            } catch (OWLOntologyCreationException e) {
                lotsa = null;
                throw new RuntimeException(e);
            } catch (IOException e) {
                lotsa = null;
                throw new RuntimeException(e);
            }
        }
        return lotsa;
    }
}
