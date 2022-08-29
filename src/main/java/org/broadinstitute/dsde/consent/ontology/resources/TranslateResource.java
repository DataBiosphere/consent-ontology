package org.broadinstitute.dsde.consent.ontology.resources;

import static org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService.TranslateFor.PARAGRAPH;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService.TranslateFor;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;
import org.slf4j.Logger;


@Path("/translate")
public class TranslateResource {

  private final Logger log = Utils.getLogger(this.getClass());
  private final TextTranslationService translationService;
  private final ObjectMapper mapper = new ObjectMapper();

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
      TranslateFor translateFor = TranslateFor.valueOf(forParam);
      return buildResponse(translateFor, restriction);
    } catch (Exception e) {
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
        final Map<String, String> body = mapper.readValue(jsonString, new TypeReference<>() {});
        String paragraph = body.get("paragraph");

        if (StringUtils.isBlank(paragraph)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Paragraph is required").build();
        }

        return buildResponse(PARAGRAPH, paragraph);
    }

    /**
     * Helper method to build a response from any form of text translation service
     *
     * @param forParam Either "purpose" or "dataset"
     * @param dataUse  JSON representation of the data use object
     * @return Response The Response
     * @throws Exception The Exception
     */
    private Response buildResponse(TranslateFor forParam, String dataUse) throws Exception {
        String result = switch(forParam) {
          case PARAGRAPH -> {
            HashMap<String, Recommendation> response = translationService.translateParagraph(dataUse);
            yield new Gson().toJson(response);
          }
          case PURPOSE -> translationService.translatePurpose(dataUse);
          case DATASET -> translationService.translateDataset(dataUse);
        };
        return Response.ok().entity(result).build();
    }

}
