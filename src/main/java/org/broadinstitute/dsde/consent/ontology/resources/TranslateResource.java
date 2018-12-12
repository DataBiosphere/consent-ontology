package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/translate")
public class TranslateResource {

    private final Logger log = LoggerFactory.getLogger(TranslateResource.class);
    private TextTranslationService helper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translate(@QueryParam("for") String forParam, String restriction) {
        try {
            return buildResponse(forParam, restriction, helper);
        } catch (IOException e) {
            log.error("Error while translating", e);
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error while translating: " + e.getMessage()).
                    build();
        }
    }

    @Inject
    public void setHelper(TextTranslationService helper) {
        this.helper = helper;
    }

    /**
     * Helper method to build a response from any form of text translation service
     *
     * @param forParam           Either "purpose" or "sampleset"
     * @param restriction        JSON representation of the translatable restriction
     * @param translationService Selected translation service
     * @return Response
     * @throws IOException       The Exception
     */
    private Response buildResponse(String forParam, String restriction, TextTranslationService translationService) throws IOException {
        if ("purpose".equals(forParam)) {
            return Response.ok().entity(translationService.translatePurpose(restriction)).build();
        }
        if ("sampleset".equals(forParam)) {
            return Response.ok().entity(translationService.translateSample(restriction)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
