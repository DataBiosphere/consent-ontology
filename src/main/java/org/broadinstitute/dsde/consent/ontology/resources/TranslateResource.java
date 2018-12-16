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
    private TextTranslationService translationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translate(@QueryParam("for") String forParam, String restriction) {
        try {
            return buildResponse(forParam, restriction);
        } catch (IOException e) {
            log.error("Error while translating", e);
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error while translating: " + e.getMessage()).
                    build();
        }
    }

    @Inject
    public void setTranslationService(TextTranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * Helper method to build a response from any form of text translation service
     *
     * @param forParam Either "purpose" or "sampleset"
     * @param datause  JSON representation of the translatable data use object
     * @return Response The Response
     * @throws IOException The Exception
     */
    private Response buildResponse(String forParam, String datause) throws IOException {
        if (TextTranslationService.TranslateFor.PURPOSE.name().equalsIgnoreCase(forParam)) {
            return Response.ok().entity(translationService.translatePurposeDataUse(datause)).build();
        }
        if (TextTranslationService.TranslateFor.DATASET.name().equalsIgnoreCase(forParam)) {
            return Response.ok().entity(translationService.translateDatasetDataUse(datause)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
