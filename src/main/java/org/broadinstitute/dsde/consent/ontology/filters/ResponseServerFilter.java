package org.broadinstitute.dsde.consent.ontology.filters;

import java.io.IOException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResponseServerFilter implements ContainerResponseFilter {

  @Override
  public void filter(
      ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
    // When the no-sniff header is on the swagger-ui files, it breaks the overall UI
    if (!requestContext.getUriInfo().getPath().contains("swagger-ui")) {
      responseContext.getHeaders().add("X-Content-Type-Options", "nosniff");
    }
  }
}