package org.broadinstitute.dsde.consent.ontology.resources;

import static org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor.PARAGRAPH;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;

@Path("/translate")
public class TranslateResource implements OntologyLogger {

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
    if (StringUtils.isBlank(restriction)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    try {
      return Response.ok().entity(translationService.translateDataUseSummary(restriction)).build();
    } catch (IllegalArgumentException iae) {
      return Response.status(Response.Status.BAD_REQUEST).entity(iae.getMessage()).build();
    } catch (Exception e) {
      logWarn("Error while translating restriction: " + e.getMessage());
      return Response.
          status(Response.Status.INTERNAL_SERVER_ERROR).
          entity("Error while translating: " + e.getMessage()).
          build();
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response translate(@QueryParam("for") String forParam, String restriction) {
    if (StringUtils.isBlank(forParam) || StringUtils.isBlank(restriction)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    try {
      TranslateFor translateFor = TranslateFor.find(forParam);
      return buildResponse(translateFor, restriction);
    } catch (IllegalArgumentException iae) {
      return Response.status(Response.Status.BAD_REQUEST).entity(iae.getMessage()).build();
    } catch (Exception e) {
      logWarn("Error while translating restriction: " + e.getMessage());
      return Response.
          status(Response.Status.INTERNAL_SERVER_ERROR).
          entity("Error while translating: " + e.getMessage()).
          build();
    }
  }

  @Path("paragraph")
  @POST
  public Response translateParagraph(final String jsonString) {
    if (StringUtils.isBlank(jsonString)) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    try {
      final Map<String, String> body = mapper.readValue(jsonString, new TypeReference<>() {
      });
      String paragraph = body.get("paragraph");
      if (StringUtils.isBlank(paragraph)) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Paragraph is required").build();
      }
      return buildResponse(PARAGRAPH, paragraph);
    } catch (JsonParseException jpe) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Paragraph is invalid").build();
    } catch (Exception e) {
      String message = "Server Error translating paragraph";
      logWarn("Error while translating paragraph: " + e.getMessage());
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), message)
          .build();
    }
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
    String result = switch (forParam) {
      case PARAGRAPH -> {
        Map<String, Recommendation> response = translationService.translateParagraph(dataUse);
        yield new Gson().toJson(response);
      }
      case PURPOSE -> translationService.translatePurpose(dataUse);
      case DATASET -> translationService.translateDataset(dataUse);
    };
    return Response.ok().entity(result).build();
  }

}
