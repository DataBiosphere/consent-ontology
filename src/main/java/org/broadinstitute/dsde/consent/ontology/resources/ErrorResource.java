package org.broadinstitute.dsde.consent.ontology.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.model.Error;
import org.eclipse.jetty.server.Request;

@Path("error")
public class ErrorResource {

  @GET
  @Path("404")
  @Produces("application/json")
  public Response notFound(@Context HttpServletRequest request) {
    String originalUri = ((Request) request).getOriginalURI();
    String msg = String.format("Unable to find requested path: '%s'", originalUri);
    Error error = new Error(msg, 404);
    return Response.status(error.getCode()).entity(error).build();
  }

}
