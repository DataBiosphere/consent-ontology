package org.broadinstitute.dsde.consent.ontology.resources;

import org.apache.commons.lang3.StringUtils;
import org.parboiled.common.FileUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/swagger")
public class SwaggerResource {

    private final static String swaggerResource = "META-INF/resources/webjars/swagger-ui/2.2.8/";

    @Context
    UriInfo uriInfo;

    @GET
    @Path("{path:.*}")
    public Response content(@PathParam("path") String path) {
        Response response;
        String mediaType = getMediaTypeFromPath(path);
        if (path.isEmpty() || path.equals("index.html")) {
            response = Response.ok().entity(getIndex()).type(mediaType).build();
        } else {
            mediaType = getMediaTypeFromPath(path);
            if (path.endsWith("png") || path.endsWith("gif")) {
                byte[] content = FileUtils.readAllBytesFromResource(swaggerResource + path);
                if (content != null) {
                    response = Response.ok().entity(content).type(mediaType).build();
                } else {
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                String content = FileUtils.readAllTextFromResource(swaggerResource + path);
                if (content != null) {
                    response = Response.ok().entity(content).type(mediaType).build();
                } else {
                    response = Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        }
        return response;
    }

    private String getMediaTypeFromPath(String path) {
        String mediaType;
        switch (StringUtils.substringAfterLast(path, ".")) {
            case "css":
                mediaType = "text/css";
                break;
            case "js":
                mediaType = "application/js";
                break;
            case "png":
                mediaType = "image/png";
                break;
            case "gif":
                mediaType = "image/gif";
                break;
            default:
                mediaType = MediaType.TEXT_HTML;
                break;
        }
        return mediaType;
    }

    private String getIndex() {
        String content = FileUtils.readAllTextFromResource(swaggerResource + "index.html");
        return content
            .replace("your-client-secret-if-required", "")
            .replace("your-realms", "Broad Institute")
            .replace("your-app-name", "Consent Ontology")
            .replace("scopeSeparator: \",\"", "scopeSeparator: \" \"")
            .replace("jsonEditor: false,", "jsonEditor: false," + "validatorUrl: null, apisSorter: \"alpha\", operationsSorter: \"alpha\",")
            .replace("url = \"http://petstore.swagger.io/v2/swagger.json\";", "url = '/api-docs/api-docs.yaml';");
    }

}
