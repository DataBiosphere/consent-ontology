package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.api.client.http.HttpStatusCodes;
import jakarta.ws.rs.BadRequestException;
import java.util.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.util.JsonSchemaUtil;
import org.parboiled.common.FileUtils;

@Path("/schemas")
public class DataUseResource implements OntologyLogger {

  private JsonSchemaUtil jsonSchemaUtil = new JsonSchemaUtil();

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

  @GET
  @Path("/data-use/v4")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSchemaV4() {
    String content = FileUtils.readAllTextFromResource("data-use-v4.json");
    return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Path("/data-use/v3")
  @Produces(MediaType.APPLICATION_JSON)
  public Response validateSchemaV3(String json) {
    try {
      List<String> errors = jsonSchemaUtil.validateDataUseV3Schema(json);
      if (errors.isEmpty()) {
        return Response.ok().type(MediaType.APPLICATION_JSON).build();
      } else {
        // nosemgrep
        return Response.status(HttpStatusCodes.STATUS_CODE_BAD_REQUEST).entity(errors)
            .type(MediaType.APPLICATION_JSON).build();
      }
    } catch (BadRequestException e) {
        return Response.status(HttpStatusCodes.STATUS_CODE_BAD_REQUEST).entity(e.getMessage())
            .type(MediaType.APPLICATION_JSON).build();
    }
  }

  @POST
  @Path("/data-use/v4")
  @Produces(MediaType.APPLICATION_JSON)
  public Response validateSchemaV4(String json) {
    try {
      List<String> errors = jsonSchemaUtil.validateDataUseV4Schema(json);
      if (errors.isEmpty()) {
        return Response.ok().type(MediaType.APPLICATION_JSON).build();
      } else {
        // nosemgrep
        return Response.status(HttpStatusCodes.STATUS_CODE_BAD_REQUEST).entity(errors)
            .type(MediaType.APPLICATION_JSON).build();
      }
    } catch (BadRequestException e) {
        return Response.status(HttpStatusCodes.STATUS_CODE_BAD_REQUEST).entity(e.getMessage())
            .type(MediaType.APPLICATION_JSON).build();
    }
  }

}
