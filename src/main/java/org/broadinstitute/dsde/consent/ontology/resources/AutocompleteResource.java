package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import java.util.Arrays;
import java.util.List;

@Path("/autocomplete")
public class AutocompleteResource {

    private AutocompleteService service;

    @Inject
    public AutocompleteResource(AutocompleteService service) {
        this.service = service;
    }

    @GET
    @Produces("application/json")
    public List<TermResource> getTerms(
            @QueryParam("q") String queryTerm,
            @QueryParam("types") @DefaultValue("") String ontologyNames,
            @QueryParam("count") @DefaultValue("20") int limit) {
        if (ontologyNames == null || ontologyNames.isEmpty()) {
            return service.lookup(queryTerm, limit);
        } else {
            return service.lookup(Arrays.asList(ontologyNames.split(",")), queryTerm, limit);
        }
    }

}
