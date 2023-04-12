package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.util.DataUseUtil;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResourceV3 {

  private final DataUseMatcherV3 dataUseMatcherV3;

  @Inject
  MatchResourceV3(DataUseMatcherV3 dataUseMatcherV3) {
    this.dataUseMatcherV3 = dataUseMatcherV3;
  }

  /**
   * Most recent version of matching logic as implemented in FireCloud
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
      if (purpose != null && dataset != null) {
        MatchResult matchResult = dataUseMatcherV3.matchPurposeAndDatasetV3(purpose, dataset);
        MatchResultType match = matchResult.getMatchResultType();
        List<String> failures = DataUseUtil.sanitize(matchResult.getMessage());
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

}
