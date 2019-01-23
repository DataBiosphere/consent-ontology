package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
