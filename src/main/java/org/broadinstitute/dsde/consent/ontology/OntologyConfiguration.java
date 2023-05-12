package org.broadinstitute.dsde.consent.ontology;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.configurations.StoreConfiguration;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OntologyConfiguration extends Configuration {

  @JsonProperty
  private final ElasticSearchConfiguration elasticSearch = new ElasticSearchConfiguration();

  @JsonProperty
  private final StoreConfiguration googleStore = new StoreConfiguration();

  public ElasticSearchConfiguration getElasticSearchConfiguration() {
    return elasticSearch;
  }

  public StoreConfiguration getCloudStoreConfiguration() {
    return googleStore;
  }

}
