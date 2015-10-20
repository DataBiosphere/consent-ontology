package org.broadinstitute.dsde.consent.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class OntologyConfiguration extends Configuration {

//    private OntologyModel ontologyList = null;
    
    public OntologyConfiguration() {
    }

    @JsonProperty
    private final ElasticSearchConfiguration elasticSearch = new ElasticSearchConfiguration();

    public ElasticSearchConfiguration getElasticSearchConfiguration() {
        return elasticSearch;
    }

    @JsonProperty
    private final CorsConfiguration cors = new CorsConfiguration();

    public CorsConfiguration getCorsConfiguration() {
        return cors;
    }

//    public OntologyModel getOntologyModel() {
//        if (ontologyList == null) {
//            try {
//                ontologyList = new OntologyList();
//            } catch (IOException ex) {
//                Logger.getLogger(OntologyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return ontologyList;    
//    }
}
