package org.broadinstitute.dsde.consent.ontology;


import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.Provides;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationServiceImpl;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateAPI;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateImpl;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class OntologyModule   extends AbstractModule {

    @Inject
    private final OntologyConfiguration config;
    @Inject
    private final Environment environment;

    public OntologyModule(OntologyConfiguration configuration, Environment environment){
        this.config = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).toInstance(config);
        bind(Environment.class).toInstance(environment);
        bind(OntologyModel.class).to(OntologyList.class).in(Scopes.SINGLETON);
        bind(UseRestrictionValidateAPI.class).to(UseRestrictionValidateImpl.class).in(Scopes.SINGLETON);
        bind(OntologyTermSearchAPI.class).to(LuceneOntologyTermSearchAPI.class).in(Scopes.SINGLETON);
        bind(TextTranslationService.class).to(TextTranslationServiceImpl.class).in(Scopes.SINGLETON);

        bind(OntologyTermSearchAPI.class).in(Singleton.class);
        bind(UseRestrictionValidateAPI.class).in(Singleton.class);
        bind(TextTranslationService.class).in(Singleton.class);
    }


    private Client getClient(ElasticSearchConfiguration config) {
        TransportClient client = new TransportClient(ImmutableSettings.settingsBuilder()
                .put("cluster.name", config.clusterName));
        for (String address : config.servers) {
            int colon = address.indexOf(':');
            int port = 9300;
            String server = address;
            if (colon >= 0) {
                port = Integer.parseInt(server.substring(colon + 1));
                server = server.substring(0, colon);
            }
            client.addTransportAddress(new InetSocketTransportAddress(server, port));
        }
        return client;
    }

    @Provides
    @Singleton
    public AutocompleteAPI providesAPI() {
        ElasticSearchConfiguration esConfig = config.getElasticSearchConfiguration();
        String index = esConfig.index;
        Client client = getClient(esConfig);
        environment.healthChecks().register("elastic-search", new ElasticSearchHealthCheck(client, index));
        return new ElasticSearchAutocompleteAPI(client, index);
    }

    @Provides
    @Singleton
    public StoreOntologyService providesStore(){
        {
            GCSStore googleStore;
            try {
                googleStore = new GCSStore(config.getCloudStoreConfiguration());
            } catch (GeneralSecurityException | IOException e) {
                throw new IllegalStateException(e);
            }
            return new StoreOntologyService(googleStore,
                    config.getStoreOntologyConfiguration().getBucketSubdirectory(),
                    config.getStoreOntologyConfiguration().getConfigurationFileName());

        }
    }
}

