package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.inject.Provider;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;

public class OntologyProvider implements Provider<OntologyModel> {

    private LuceneOntologyTermSearchAPI lotsa = null;
    private OntologyModel ontologyList;
    
//    public static synchronized LuceneOntologyTermSearchAPI getLuceneOntologyTermSearchAPI() {
//        
//        int count = 3;
//        // retry a few times if it fails...
//        while (lotsa == null && count-- > 0) {
//            try {
//                Collection<String> resources = ontologyList.getResources();
//                Iterator<String> resourceIter = resources.iterator();
//                InputStream[] ontologies = new InputStream[resources.size()];
//                for (int i = 0; i < resources.size() && resourceIter.hasNext(); i++) {
//                    ontologies[i] = Resources.getResource(resourceIter.next()).openStream();
//                }
//                lotsa = new LuceneOntologyTermSearchAPI(new RAMDirectory(), ontologies);
//            } catch (OWLOntologyCreationException | IOException e) {
//                lotsa = null;
//                throw new RuntimeException(e);
//            }
//        }
//        return lotsa;
//    }

//    @Inject
//    public static void setOntologyList(OntologyModel ontologyList) {
//        OntologyProvider.ontologyList = ontologyList;
//    }
//
//    @Inject
//    public static void setLotsa(LuceneOntologyTermSearchAPI lotsa) {
//        OntologyProvider.lotsa = lotsa;
//    }
//    

    @Override
    public OntologyModel get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
