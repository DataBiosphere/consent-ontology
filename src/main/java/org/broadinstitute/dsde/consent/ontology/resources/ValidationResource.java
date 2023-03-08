package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.service.dataUseValidationService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/validation")
@Consumes("application/json")
@Produces("application/json")
public class ValidationResource {

  private dataUseValidationService service;

  @Inject
  public ValidationResource(dataUseValidationService service) {
    this.service = service;
  }

  @POST
  @Path("/data-use/v3")
  public Response validateDataUse(String dataUse){
    try{
      return Response.ok().entity(service.validateDataUse(dataUse)).build();
    } catch (Exception e){
      return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
    }
  }

}