package org.broadinstitute.dsde.consent.ontology.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.broadinstitute.dsde.consent.ontology.datause.ontology.TranslationHelper;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

@Path("/translate")
public class TranslateResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translate(
            @QueryParam("for") String forParam,
            String restriction ) {
        try {
            TranslationHelper helper = new TranslationHelper();
            if ( "purpose".equals(forParam) )
                return Response.ok().entity(helper.translatePurpose(restriction)).build();
            if ( "sampleset".equals(forParam) )
                return Response.ok().entity(helper.translateSample(restriction)).build();
        } catch (IOException | OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
