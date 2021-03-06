package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/search")
public class OntologySearchResource {

    private AutocompleteService service;

    @Inject
    public OntologySearchResource(AutocompleteService service) {
        this.service = service;
    }

    @GET
    @Produces("application/json")
    public Response getOntologyById(@QueryParam("id") @DefaultValue("") String queryTerm) throws IOException {
        if (!queryTerm.isEmpty()) {
            List<TermResource> result = service.lookupById(queryTerm);
            if(!result.isEmpty()){
                return Response.ok().entity(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(" Supplied ID doesn't match any known ontology. ", Response.Status.NOT_FOUND.getStatusCode()))
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(" Ontology ID term cannot be empty. ", Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        }
    }
}
