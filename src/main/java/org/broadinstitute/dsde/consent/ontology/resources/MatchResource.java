package org.broadinstitute.dsde.consent.ontology.resources;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerActor;
import org.broadinstitute.dsde.consent.ontology.actor.MatchWorkerMessage;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.Collection;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResource {

    private final Logger log = Utils.getLogger(this.getClass());
    private StoreOntologyService storeOntologyService;
    private DataUseMatcher dataUseMatcher;

    private static final ActorSystem actorSystem = ActorSystem.create("actorSystem");
    private static final ActorRef matchWorkerActor = actorSystem.actorOf(Props.create(MatchWorkerActor.class), "MatchWorkerActor");

    MatchResource() {}

    /**
     * Most recent version of matching logic as implemented in FireCloud
     *
     * @param matchPair The DataUseMatchPair
     * @return Response
     */
    @Path("/v2")
    @POST
    public Response matchDataUse(final DataUseMatchPair matchPair) {
        DataUse purpose = matchPair.getPurpose();
        DataUse dataset = matchPair.getConsent();
        try {
            if (purpose != null && dataset != null) {
                boolean match = dataUseMatcher.matchPurposeAndDataset(purpose, dataset);
                return Response
                        .ok()
                        .entity(ImmutableMap.of("result", match, "matchPair", matchPair))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            ErrorResponse error = new ErrorResponse();
            error.setCode(Response.Status.BAD_REQUEST.getStatusCode());
            if (purpose == null) {
                error.setMessage("Purpose is required");
            } else {
                error.setMessage("Dataset is required");
            }
            return Response.status(error.getCode()).entity(error).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.serverError().entity(e).type(MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * V1 pointer to original implementation of matching logic. Should remain supported until no longer in use.
     *
     * @param response AsyncResponse
     * @param matchPair MatchPair
     * @throws Exception The Exception
     */
    @Deprecated
    @Path("/v1")
    @POST
    public void matchV1(@Suspended final AsyncResponse response, final MatchPair matchPair) throws Exception {
        match(response, matchPair);
    }

    /**
     * Original implementation of matching logic. Should remain supported until no longer in use.
     *
     * @param response AsyncResponse
     * @param matchPair MatchPair
     * @throws Exception The Exception
     */
    @Deprecated
    @POST
    public void match(@Suspended final AsyncResponse response, final MatchPair matchPair) throws Exception {
        Timeout timeout = new Timeout(Duration.create(15000, "seconds"));
        Collection<URL> urls = storeOntologyService.retrieveOntologyURLs();
        final MatchWorkerMessage matchMessage = new MatchWorkerMessage(urls, matchPair);
        final Future<Object> matchFuture = Patterns.ask(matchWorkerActor, matchMessage, timeout);
        matchFuture.onComplete(
            new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable failure, Object result) {
                    if (failure != null) {
                        log.error("Failure: " + failure.getMessage());
                        if(failure instanceof InternalError){
                            response.resume(new WebApplicationException(failure.getMessage(), Response.Status.INTERNAL_SERVER_ERROR));
                        }
                        response.resume(new WebApplicationException(failure));
                    } else{
                        response.resume(ImmutableMap.of("result", result, "matchPair", matchPair));
                    }
                }
            },
            actorSystem.dispatcher()
        );
    }

    @Inject
    public void setStoreOntologyService(StoreOntologyService storeOntologyService) {
        this.storeOntologyService = storeOntologyService;
    }

    @Inject
    public void setDataUseMatcher(DataUseMatcher dataUseMatcher) {
        this.dataUseMatcher = dataUseMatcher;
    }

}
