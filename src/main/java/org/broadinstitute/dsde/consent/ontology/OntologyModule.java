package org.broadinstitute.dsde.consent.ontology;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import io.dropwizard.setup.Environment;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationServiceImpl;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateAPI;
import org.broadinstitute.dsde.consent.ontology.service.validate.UseRestrictionValidateImpl;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;

public class OntologyModule extends AbstractModule {
    private static Logger LOG = Logger.getLogger(OntologyModule.class);
    
    @Override
    protected void configure() {
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
            LOG.debug("Elastic search server : " + address);
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
    public AutocompleteAPI providesAPI(Environment env, OntologyConfiguration config) {
        ElasticSearchConfiguration esConfig = config.getElasticSearchConfiguration();
        String index = esConfig.index;
        Client client = getClient(esConfig);
        env.healthChecks().register("elastic-search", new ElasticSearchHealthCheck(client, index));
        return new ElasticSearchAutocompleteAPI(client, index);
    }
}
