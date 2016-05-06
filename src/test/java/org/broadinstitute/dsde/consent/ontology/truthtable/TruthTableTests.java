package org.broadinstitute.dsde.consent.ontology.truthtable;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.mockito.InjectMocks;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * See https://docs.google.com/document/d/1xyeYoIKBDFGAsQ_spoK5Ye5esMOXqpRBojd6ijZWJkk
 * for a summary of use cases for which this test class covers.
 */
class TruthTableTests extends AbstractTest {

    Future<Response> getResponseFuture(ResourceTestRule RULE, MatchPair pair) {
        return RULE.getJerseyTest().target("/match")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .async()
            .post(Entity.json(pair));
    }

    void assertResponse(Future<Response> responseFuture, Boolean expected) {
        try {
            Response response = responseFuture.get();
            String responseString = response.readEntity(String.class);
            assertTrue((responseString.contains("\"result\":true")) == expected);
            assertTrue((response.getStatus() >= 200));
            assertTrue((response.getStatus() < 300));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("Should not have received a failure.");
        }
    }

}
