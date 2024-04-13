package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV4;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.datause.MatchV3ResponseEntity;
import org.broadinstitute.dsde.consent.ontology.datause.MatchV4ResponseEntity;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV4;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV4;

@Path("/match")
@Consumes("application/json")
@Produces("application/json")
public class MatchResource {

  private final DataUseMatcherV3 dataUseMatcherV3;
  private final DataUseMatcherV4 dataUseMatcherV4;

  @Inject
  MatchResource(DataUseMatcherV3 dataUseMatcherV3, DataUseMatcherV4 dataUseMatcherV4) {
    this.dataUseMatcherV3 = dataUseMatcherV3;
    this.dataUseMatcherV4 = dataUseMatcherV4;
  }

  /**
   * Matching logic as implemented in FireCloud
   *
   * @param matchPair The DataUseMatchPair
   * @return Response
   */
  @Path("/v2")
  @POST
  @Deprecated
  public Response matchDataUse(final DataUseMatchPair matchPair) {
    return Response.status(Status.GONE).build();
  }

  /**
   * Version 3 of matching logic
   *
   * @param matchPair The DataUseMatchPairV3
   * @return Response
   */
  @Path("/v3")
  @POST
  @Deprecated
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
        List<String> rationale = matchResult.getMessage();
        // nosemgrep
        return Response
            .ok()
            .entity(new MatchV3ResponseEntity(match, matchPair, rationale).get())
            .type(MediaType.APPLICATION_JSON)
            .build();
      }
      return Response.status(error.getCode()).entity(error).type(MediaType.APPLICATION_JSON)
          .build();
    } catch (Exception e) {
      return Response.serverError().entity(e).type(MediaType.APPLICATION_JSON).build();
    }
  }

  /**
   * Version 4 of matching logic
   *
   * @param matchPair The DataUseMatchPairV4
   * @return Response
   */
  @Path("/v4")
  @POST
  public Response matchDataUseV4(final DataUseMatchPairV4 matchPair) {
    DataUseV4 purpose = matchPair.getPurpose();
    DataUseV4 dataset = matchPair.getConsent();
    try {
      ErrorResponse error = new ErrorResponse();
      error.setCode(Response.Status.BAD_REQUEST.getStatusCode());
      if (purpose == null) {
        error.setMessage("Purpose is required");
      } else {
        error.setMessage("Dataset is required");
      }
      if (purpose != null && dataset != null) {
        MatchResult matchResult = dataUseMatcherV4.matchPurposeAndDatasetV4(purpose, dataset);
        MatchResultType match = matchResult.getMatchResultType();
        List<String> rationale = matchResult.getMessage();
        return Response
            .ok()
            .entity(new MatchV4ResponseEntity(match, matchPair, rationale).get())
            .type(MediaType.APPLICATION_JSON)
            .build();
      }
      return Response.status(error.getCode()).entity(error).type(MediaType.APPLICATION_JSON)
          .build();
    } catch (Exception e) {
      return Response.serverError().entity(e).type(MediaType.APPLICATION_JSON).build();
    }
  }

}
