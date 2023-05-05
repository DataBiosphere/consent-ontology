package org.broadinstitute.dsde.consent.ontology.configurations;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ElasticSearchConfiguration {

    @NotNull
    public List<String> servers;

    @NotNull
    public String index;

    /**
     * This is configurable for testing purposes
     */
    private int port = 9200;

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
