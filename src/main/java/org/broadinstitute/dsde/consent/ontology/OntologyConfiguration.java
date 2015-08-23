package org.broadinstitute.dsde.consent.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class OntologyConfiguration extends Configuration {

    public OntologyConfiguration() {}

    @JsonProperty
    private ElasticSearchConfiguration elasticSearch = new ElasticSearchConfiguration();

    public ElasticSearchConfiguration getElasticSearchConfiguration() {
        return elasticSearch;
    }

    @JsonProperty
    private CorsConfiguration cors = new CorsConfiguration();

    public CorsConfiguration getCorsConfiguration() {
        return cors;
    }

}
