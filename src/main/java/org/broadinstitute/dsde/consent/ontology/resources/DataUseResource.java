package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseValidator;
import org.parboiled.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.stream.Collectors;

@Path("/schemas")
public class DataUseResource {

    Logger log = LoggerFactory.getLogger(DataUseResource.class);

    @GET
    @Path("/data-use")
    @Produces("application/json")
    public Response getSchema() {
        String content = FileUtils.readAllTextFromResource("data-use.json");
        return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
    }

    /**
     * This method will take any valid form of json string that models DataUse.
     * With that object, it generates a UseRestriction using current business rules as specified in:
     * https://docs.google.com/document/d/1z4yXkWRrg2ngyIX2otxG2lV_fJE_fYSs4VxmVjyp_pU/edit#heading=h.qifwhouc9pi5
     *
     * @param jsonString String that will conform to DataUse
     * @return Response
     */
    @POST
    @Path("/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response translate(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataUse dataUse = mapper.readValue(jsonString, DataUse.class);
            DataUseValidator validator = new DataUseValidator(dataUse);
            if (validator.getIsValid()) {
                UseRestriction restriction = translateSchema(dataUse);
                return Response.ok().entity(restriction).type(MediaType.APPLICATION_JSON).build();
            } else {
                String message = "Data Use does not meet validation rules:\n" +
                    validator.getValidationErrors().stream().collect(Collectors.joining("\n"));
                log.error(message);
                return Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.APPLICATION_JSON).build();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e).type(MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * TODO: Flesh this out in a utility that will generate appropriate UseRestrictions for all of
     * the possible cases of DataUse options.
     * See https://broadinstitute.atlassian.net/browse/GAWB-1555
     *
     * @param schema The DataUse
     * @return Translated UseRestriction
     */
    private UseRestriction translateSchema(DataUse schema) {
        if (schema.getGeneralUse()) {
            return new Everything();
        }
        return null;
    }

}
