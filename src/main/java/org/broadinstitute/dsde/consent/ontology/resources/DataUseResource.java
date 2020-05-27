package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.datause.builder.ConsentRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.builder.DARRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilder;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.parboiled.common.FileUtils;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/schemas")
public class DataUseResource {

    private final Logger log = Utils.getLogger(this.getClass());

    @GET
    @Path("/data-use")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchema() {
        String content = FileUtils.readAllTextFromResource("data-use.json");
        return Response.ok().entity(content).type(MediaType.APPLICATION_JSON).build();
    }

    /**
     * This endpoint will take any valid form of json string that models DataUse and generate a Consent
     * UseRestriction using current business rules.
     *
     * @param jsonString String that will conform to DataUse
     * @return Response
     */
    @POST
    @Path("/data-use/consent/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response translateConsent(String jsonString) {
        return buildTranslatedResponse(jsonString, new ConsentRestrictionBuilder());
    }

    /**
     * This endpoint will take any valid form of json string that models DataUse and generate a Data Access Request
     * UseRestriction using current business rules.
     *
     * @param jsonString String that will conform to DataUse
     * @return Response
     */
    @POST
    @Path("/data-use/dar/translate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response translateDAR(String jsonString) {
        return buildTranslatedResponse(jsonString, new DARRestrictionBuilder());
    }

    private Response buildTranslatedResponse(String jsonString, UseRestrictionBuilder builder) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataUse schema = mapper.readValue(jsonString, DataUse.class);
            UseRestriction restriction = builder.buildUseRestriction(schema);
            return Response.ok().entity(restriction).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).type(MediaType.APPLICATION_JSON).build();
        }
    }

}
