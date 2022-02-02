package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.apache.commons.lang3.stream.Streams;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/search")
public class OntologySearchResource {

    private final AutocompleteService service;

    @Inject
    public OntologySearchResource(AutocompleteService service) {
        this.service = service;
    }

    @GET
    @Produces("application/json")
    public Response getOntologyById(@QueryParam("id") @DefaultValue("") String queryTerm) {
        if (queryTerm.isBlank()) {
            return Response
                    .status(Response.Status.BAD_REQUEST.getStatusCode(), "Ontology ID term cannot be empty.")
                    .build();
        }

        String[] queries = queryTerm.split(",");

        try {
            List<TermResource> results = new Streams.FailableStream<>(Arrays.stream(queries))
                    .filter(Objects::nonNull)
                    .filter(q -> !q.isBlank())
                    .map(service::lookupById)
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            return checkOntologyRetrieval(results);
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Ontology could not be successfully retrieved.")
                    .build();
        }

    }

    private Response checkOntologyRetrieval(List<TermResource> results) {
        if (results.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND.getStatusCode(), "Supplied IDs do not match any known ontology.")
                    .build();
        } else {
            return Response.ok().entity(results).build();
        }
    }
}
