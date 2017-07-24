package org.broadinstitute.dsde.consent.ontology.service;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

public class ElasticSearchSupport {

    public static RestClient getRestClient(ElasticSearchConfiguration configuration) {
        HttpHost[] hosts = configuration.
            getServers().
            stream().
            map(server -> new HttpHost(server, 9200, "http")).
            collect(Collectors.toList()).toArray(new HttpHost[configuration.getServers().size()]);
        return RestClient.builder(hosts).build();
    }

    public static String getIndexPath(String index) {
        return "/" + index;
    }

    public static String getSearchPath(String index) throws UnsupportedEncodingException {
        return "/" + index + "/ontology_term/_search";
    }

    public static String getClusterHealthPath(String index) {
        return "/_cluster/health/" + index;
    }

    public static Header jsonHeader = new BasicHeader("Content-Type", "application/json");

}
