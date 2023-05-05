package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResource {

    private final DataUseMatcher dataUseMatcher;

    private final DataUseMatcherV3 dataUseMatcherV3;

    @Inject
    MatchResource(DataUseMatcher dataUseMatcher, DataUseMatcherV3 dataUseMatcherV3) {
        this.dataUseMatcher = dataUseMatcher;
        this.dataUseMatcherV3 = dataUseMatcherV3;
    }

    /**
     * Matching logic as implemented in FireCloud
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
   * Version 3 of matching logic
   *
   * @param matchPair The DataUseMatchPairV3
   * @return Response
   */
  @Path("/v3")
  @POST
  public Response matchDataUseV3(final DataUseMatchPairV3 matchPair) {
    DataUseV3 purpose = matchPair.getPurpose();
    DataUseV3 dataset = matchPair.getConsent();
    try {
      ErrorResponse error = new ErrorResponse();
      error.setCode(Response.Status.BAD_REQUEST.getStatusCode());
      if (purpose == null) {
        error.setMessage("Purpose is required");
      } else {
        error.setMessage("Dataset is required");
      }
      if (purpose != null && dataset != null) {
        MatchResult matchResult = dataUseMatcherV3.matchPurposeAndDatasetV3(purpose, dataset);
        MatchResultType match = matchResult.getMatchResultType();
        List<String> failures = matchResult.getMessage();
        // nosemgrep
        return Response
            .ok()
            .entity(ImmutableMap.of(
                "result", match,
                "matchPair", matchPair,
                "failureReasons", failures))
            .type(MediaType.APPLICATION_JSON)
            .build();
      }
      return Response.status(error.getCode()).entity(error).type(MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      return Response.serverError().entity(e).type(MediaType.APPLICATION_JSON).build();
    }
  }

}
