package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.translate.DTO.RecommendationDto;
import org.broadinstitute.dsde.consent.ontology.translate.service.Translate;
import org.slf4j.Logger;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Path("/translate")
public class TranslateResource {

    private final Logger log = Utils.getLogger(this.getClass());
    private TextTranslationService translationService;

    @Inject
    public TranslateResource(TextTranslationService translationService) {
        this.translationService = translationService;
    }

    @POST
    @Path("summary")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translateSummary(String restriction) {
        try {
            return Response.ok().entity(translationService.translateDataUseSummary(restriction)).build();
        } catch (Exception e) {
            log.error("Error while translating", e);
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("Error while translating: " + e.getMessage()).
                    build();
        }
    }

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

    @Path("paragraph")
    @POST
    public Response translateParagraph(final String jsonString) throws Exception {
        Translate translate = new Translate();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = mapper.readValue(jsonString, new TypeReference<>() {});
        String paragraph = (String) body.get("paragraph");

        if (StringUtils.isBlank(paragraph)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Paragraph is required").build();
        }

        HashMap<String, RecommendationDto> recommendations = translate.paragraph(paragraph);

        return Response.ok(recommendations).build();
    }

    /**
     * Helper method to build a response from any form of text translation service
     *
     * @param forParam Either "purpose" or "dataset"
     * @param dataUse  JSON representation of the data use object
     * @return Response The Response
     * @throws IOException The Exception
     */
    private Response buildResponse(String forParam, String dataUse) throws IOException {
        if (TextTranslationService.TranslateFor.PURPOSE.name().equalsIgnoreCase(forParam)) {
            return Response.ok().entity(translationService.translatePurpose(dataUse)).build();
        }
        if (TextTranslationService.TranslateFor.DATASET.name().equalsIgnoreCase(forParam)) {
            return Response.ok().entity(translationService.translateDataset(dataUse)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
