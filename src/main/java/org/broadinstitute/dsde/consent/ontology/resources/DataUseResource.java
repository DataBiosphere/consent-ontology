package org.broadinstitute.dsde.consent.ontology.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.parboiled.common.FileUtils;

@Path("/schemas")
public class DataUseResource implements OntologyLogger {

    @GET
    @Path("/data-use")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchema() {
        String content = FileUtils.readAllTextFromResource("data-use.json");
        return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/data-use/v2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemaV2() {
        return getSchema();
    }

    @GET
    @Path("/data-use/v3")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemaV3() {
        String content = FileUtils.readAllTextFromResource("data-use-v3.json");
        return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
    }

}
