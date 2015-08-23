package org.broadinstitute.dsde.consent.ontology;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.setup.Environment;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;

public class OntologyModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    private Client getClient(ElasticSearchConfiguration config) {
        TransportClient client = new TransportClient(ImmutableSettings.settingsBuilder()
                .put("cluster.name", config.clusterName));
        for(String address: config.servers) {
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
