package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.model.MatchWorkerMessage;
import org.broadinstitute.dsde.consent.ontology.service.OntModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResource {

    private final StoreOntologyService storeOntologyService;
    private final DataUseMatcher dataUseMatcher;
    private final OntModelFactory ontModelFactory = OntModelFactory.INSTANCE;

    @Inject
    MatchResource(DataUseMatcher dataUseMatcher, StoreOntologyService storeOntologyService) {
        this.dataUseMatcher = dataUseMatcher;
        this.storeOntologyService = storeOntologyService;
    }

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
                ImmutablePair<Boolean, List<String>> matchResult = dataUseMatcher.matchPurposeAndDatasetV2(purpose, dataset);
                boolean match = matchResult.getLeft();
                List<String> failures = matchResult.getRight();
                return Response
                        .ok()
                        .entity(ImmutableMap.of(
                                "result", match,
                                "matchPair", matchPair,
                                "failureReasons", failures))
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
        Collection<URL> urls = storeOntologyService.retrieveOntologyURLs();
        final MatchWorkerMessage matchMessage = new MatchWorkerMessage(urls, matchPair);
        try {
            Boolean match = ontModelFactory.matchPurpose(matchMessage);
            response.resume(ImmutableMap.of("result", match, "matchPair", matchPair));
        } catch (Exception e) {
            response.resume(new WebApplicationException(e));
        }
    }

}
