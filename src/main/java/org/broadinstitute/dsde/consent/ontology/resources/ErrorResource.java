package org.broadinstitute.dsde.consent.ontology.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import org.broadinstitute.dsde.consent.ontology.model.Error;

@Path("error")
public class ErrorResource {

  @GET
  @Path("404")
  @Produces("application/json")
  public Response notFound(@Context HttpServletRequest request) {
    String originalUri = request.getRequestURI();
    String decodedUri = URLDecoder.decode(originalUri, Charset.defaultCharset());
    String msg = String.format("Unable to find requested path: '%s'", decodedUri);
    Error error = new Error(msg, 404);
    return Response.status(error.code()).entity(error).build();
  }

}
