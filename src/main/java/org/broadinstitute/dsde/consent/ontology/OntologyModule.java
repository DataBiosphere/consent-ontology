package org.broadinstitute.dsde.consent.ontology;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationServiceImpl;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocomplete;

public class OntologyModule extends AbstractModule {

  private final OntologyConfiguration config;
  private final Environment environment;

  OntologyModule(OntologyConfiguration configuration, Environment environment) {
    this.config = configuration;
    this.environment = environment;
  }

  @Override
  protected void configure() {
    bind(Configuration.class).toInstance(config);
    bind(Environment.class).toInstance(environment);
  }

  @Provides
  @Singleton
  public TextTranslationService providesTextTranslationService() {
    return new TextTranslationServiceImpl(providesAutocomplete(), providesGCSStore());
  }

  @Provides
  @Singleton
  public AutocompleteService providesAutocomplete() {
    return new ElasticSearchAutocomplete(config.getElasticSearchConfiguration());
  }

  @Provides
  @Singleton
  public GCSStore providesGCSStore() {
    try {
      return new GCSStore(config.getCloudStoreConfiguration());
    } catch (GeneralSecurityException | IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Provides
  public ElasticSearchConfiguration providesElasticSearchConfiguration() {
    return config.getElasticSearchConfiguration();
  }

  @Provides
  public HealthCheckRegistry providesHealthCheckRegistry() {
    return environment.healthChecks();
  }

}

