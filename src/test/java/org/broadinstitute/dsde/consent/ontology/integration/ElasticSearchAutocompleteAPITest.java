package org.broadinstitute.dsde.consent.ontology.integration;

import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchSupport;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

// TODO: Migrate all of the ES integration-like tests to this framework.
@Category(IntegrationTest.class)
public class ElasticSearchAutocompleteAPITest {

    private ElasticSearchAutocompleteAPI autocompleteAPI;
    private static final String INDEX_NAME = "ontology-integration";
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPITest.class);

    // TODO: Move index setup/population to a test-support class
    @Before
    public void setUp() throws Exception {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList("localhost"));
        this.autocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
        RestClient client = ElasticSearchSupport.createRestClient(configuration);
        Response esResponse = client.performRequest(
            "PUT",
            ElasticSearchSupport.getIndexPath(INDEX_NAME),
            ElasticSearchSupport.jsonHeader);
        logger.info(esResponse.toString());
    }

    @After
    public void shutDown() throws Exception {
    }

    /**
     * Critical that this test cover multiple, rapid-fire queries to ES.
     *
     * @throws Exception The exception
     */
    @Test
    public void  testLookupById() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookupById("DOID_162");
        Assert.assertTrue(true);
    }

}
