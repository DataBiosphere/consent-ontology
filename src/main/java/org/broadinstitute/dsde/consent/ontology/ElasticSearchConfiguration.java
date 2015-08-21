package org.broadinstitute.dsde.consent.ontology;

import java.util.List;

public class ElasticSearchConfiguration {
    public List<String> servers;
    public String clusterName = "elasticsearch";
    public String index;
}
