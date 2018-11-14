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
    final static String MEDIA_TYPE_CSS = new MediaType("text", "css").toString();
    final static String MEDIA_TYPE_JS = new MediaType("application", "js").toString();
    final static String MEDIA_TYPE_PNG = new MediaType("image", "png").toString();
    final static String MEDIA_TYPE_GIF = new MediaType("image", "gif").toString();

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
                if (StringUtils.isNotEmpty(content)) {
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
                mediaType = MEDIA_TYPE_CSS;
                break;
            case "js":
                mediaType = MEDIA_TYPE_JS;
                break;
            case "png":
                mediaType = MEDIA_TYPE_PNG;
                break;
            case "gif":
                mediaType = MEDIA_TYPE_GIF;
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
