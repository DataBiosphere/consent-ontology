package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseSchema;
import org.parboiled.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/schema")
public class SchemaResource {

    Logger log = LoggerFactory.getLogger(SchemaResource.class);

    @GET
    @Produces("application/json")
    public Response getSchema() {
        String content = FileUtils.readAllTextFromResource("data-use-schema.json");
        return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
    }

    /**
     * This method will take any valid form of json string that models DataUseSchema.
     * With that schema, it generates a UseRestriction according to current business rules
     * as specified in a series of truth tables:
     * https://docs.google.com/document/d/1z4yXkWRrg2ngyIX2otxG2lV_fJE_fYSs4VxmVjyp_pU/edit#heading=h.qifwhouc9pi5
     *
     * @param schemaString String that will conform to DataUseSchema
     * @return Response
     */
    @POST
    @Path("/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response translate(String schemaString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataUseSchema schema = mapper.readValue(schemaString, DataUseSchema.class);
            UseRestriction restriction = translateSchema(schema);
            return Response.ok().entity(restriction).type(MediaType.APPLICATION_JSON).build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e).type(MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     * TODO: Flesh this out in a utility that will generate appropriate UseRestrictions for all of
     * the possible cases of DataUseSchema options.
     *
     * @param schema The DataUseSchema
     * @return Translated UseRestriction
     */
    private UseRestriction translateSchema(DataUseSchema schema) {
        if (schema.getGeneralUse()) {
            return new Everything();
        }
        return null;
    }

}
