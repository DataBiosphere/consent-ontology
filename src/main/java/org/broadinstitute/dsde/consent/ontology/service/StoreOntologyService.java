package org.broadinstitute.dsde.consent.ontology.service;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.cloudstore.CloudStore;


/**
 * Created by SantiagoSaucedo on 3/11/2016.
 */
public class StoreOntologyService {

    private final CloudStore store;
    private final String bucketSubdirectory;
    private final String configurationFileName;
    private final String jsonExtension = ".json";
    protected Logger logger() {
        return Logger.getLogger("GCSStore");
    }


    public StoreOntologyService(CloudStore store, String bucketSubdirectory, String configurationFileName) {
        this.store = store;
        this.bucketSubdirectory = bucketSubdirectory;
        this.configurationFileName = configurationFileName;
    }

    public String retrieveConfigurationFile() {
        String suffix = bucketSubdirectory + configurationFileName + jsonExtension;
        try {
            HttpResponse response = store.getStorageDocument(suffix);
            return response.parseAsString();
        } catch (Exception e) {
            if (e instanceof HttpResponseException && ((HttpResponseException) e).getStatusCode() == 404) {
                logger().error("Storage service did not find Ontology configuration file: " + suffix);
            }else{
                logger().error("Problem with storage service. "+ e.getMessage());
            }
                throw new InternalError("Problem with storage service.");
        }
    }

    public HttpResponse retrieveFile(String fileUrl) {
        try {
            return  store.getDocument(fileUrl);
        } catch (Exception e) {
            if (e instanceof HttpResponseException && ((HttpResponseException) e).getStatusCode() == 404) {
                logger().error("Storage service did not find Ontology file: " + fileUrl);
            }else{
                logger().error("Problem with storage service. "+ e.getMessage());
            }
            throw new InternalError("Problem with storage service.");
        }
    }
}

