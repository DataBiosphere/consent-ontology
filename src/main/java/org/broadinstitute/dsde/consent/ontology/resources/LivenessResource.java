package org.broadinstitute.dsde.consent.ontology.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("liveness")
public class LivenessResource {

  @GET
  @Produces("text/plain")
  public Response getStatus() {
    return Response.ok("Healthy!").build();
  }
}