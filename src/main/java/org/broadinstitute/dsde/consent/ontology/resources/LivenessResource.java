package org.broadinstitute.dsde.consent.ontology.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("liveness")
public class LivenessResource {

    @GET
    @Produces("text/plain")
    public Response getStatus() {
        return Response.ok("Healthy!").build();
    }
}