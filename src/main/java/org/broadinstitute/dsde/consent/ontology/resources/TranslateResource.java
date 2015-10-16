package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.ontology.TranslationHelper;

@Path("/translate")
public class TranslateResource {

    private static Logger LOG = Logger.getLogger(TranslateResource.class);
    private TranslationHelper helper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response translate(
            @QueryParam("for") String forParam,
            String restriction) {
        try {
            if ("purpose".equals(forParam)) {
                return Response.ok().entity(helper.translatePurpose(restriction)).build();
            }
            if ("sampleset".equals(forParam)) {
                return Response.ok().entity(helper.translateSample(restriction)).build();
            }
        } catch (IOException e) {
            LOG.error("Error while translating", e);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Inject
    public void setHelper(TranslationHelper helper) {
        this.helper = helper;
    }
}
