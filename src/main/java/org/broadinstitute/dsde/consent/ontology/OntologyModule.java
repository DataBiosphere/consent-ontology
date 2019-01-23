package org.broadinstitute.dsde.consent.ontology;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSHealthCheck;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocomplete;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidationService;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class OntologyModule extends AbstractModule {

    private final OntologyConfiguration config;
    private final Environment environment;

    OntologyModule(OntologyConfiguration configuration, Environment environment){
        this.config = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(config);
        bind(Environment.class).toInstance(environment);
        bind(TextTranslationService.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public UseRestrictionValidationService providesUseRestrictionValidationService() {
        LuceneOntologyTermSearchAPI service = new LuceneOntologyTermSearchAPI(providesStore());
        return new UseRestrictionValidator(service);
    }

    @Provides
    @Singleton
    public AutocompleteService providesAutocomplete() {
        return new ElasticSearchAutocomplete(config.getElasticSearchConfiguration());
    }

    @Provides
    @Singleton
    public StoreOntologyService providesStore() {
        GCSStore googleStore;
        try {
            googleStore = new GCSStore(config.getCloudStoreConfiguration());
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException(e);
        }
        environment.healthChecks().register("google-cloud-storage", new GCSHealthCheck(googleStore));
        return new StoreOntologyService(googleStore,
            config.getStoreOntologyConfiguration().getBucketSubdirectory(),
            config.getStoreOntologyConfiguration().getConfigurationFileName());
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

