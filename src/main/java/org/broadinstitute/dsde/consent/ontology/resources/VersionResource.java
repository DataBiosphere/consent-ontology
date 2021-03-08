package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.model.Version;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

@Path("/version")
public class VersionResource {

    private final Logger log = Utils.getLogger(this.getClass());

    @GET
    @Produces("application/json")
    public Response content() {
        Version version = new Version(getGitProperties());
        return Response.ok().entity(version).build();
    }

    private String getGitProperties() {
        try {
            return IOUtils.resourceToString("/git.properties", Charset.defaultCharset());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
