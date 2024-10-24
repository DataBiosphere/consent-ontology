package org.broadinstitute.dsde.consent.ontology.resources;

import java.nio.charset.Charset;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.model.Version;

@Path("/version")
public class VersionResource implements OntologyLogger {

  @GET
  @Produces("application/json")
  public Response content() {
    Version version = new Version(getGitProperties());
    return Response.ok().entity(version).build();
  }

  private String getGitProperties() {
    try {
      return IOUtils.resourceToString("/git.properties", Charset.defaultCharset());
    } catch (Exception e) {
      logException(e);
    }
    return null;
  }

}
