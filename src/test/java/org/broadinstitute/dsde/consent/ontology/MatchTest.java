package org.broadinstitute.dsde.consent.ontology;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.broadinstitute.dsde.consent.ontology.actor.OntologyMatchingActor;
import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.broadinstitute.dsde.consent.ontology.resources.MatchResource;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MatchTest {

    /**
     * Boilerplate for setting up the Match Resource.
     */
    private static final ActorSystem actorSystem = ActorSystem.create("actorSystem");
    private static final ActorRef ontologyMatchingActor =
        actorSystem.actorOf(Props.create(OntologyMatchingActor.class), "OntologyMatchingActor");

    /**
     * Using a GrizzlyTestContainerFactory here because the standard jersey in-memory container cannot
     * process async requests.
     */
    @ClassRule
    public static final ResourceTestRule RULE =
        ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new MatchResource(ontologyMatchingActor))
            .build();

    @Test
    public void testMatch() {

        MatchPair pair = new MatchPair(
            new And(
                new Named("http://www.broadinstitute.org/ontologies/DURPO/methods_research"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/women")
            ),
            new Everything());

        Future<Response> responseFuture = RULE.getJerseyTest().target("/match")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .async()
                .post(Entity.json(pair));

        try {
            Response response = responseFuture.get();
            assertThat(response.getStatus()).isEqualTo(200);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("Should not have received a failure.");
        }

    }

}
