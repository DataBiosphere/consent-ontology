package org.broadinstitute.dsde.consent.ontology.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.cloudstore.CloudStore;

/**
 * Created by SantiagoSaucedo on 3/11/2016.
 */
@SuppressWarnings("FieldCanBeLocal")
public class StoreOntologyService implements OntologyLogger {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private final CloudStore store;
    private final String bucketSubdirectory;
    private final String configurationFileName;
    private final String jsonExtension = ".json";

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
                logError("Storage service did not find Ontology configuration file: " + suffix);
            } else {
                logException("Problem with storage service.", e);
            }
        }
        return "";
    }

    public Collection<URL> retrieveOntologyURLs() throws IOException {
        return retrieveConfigurationKeys().
            stream().
            map(str -> {
                try {
                    return new URL(str);
                } catch (MalformedURLException e) {
                    logError("Unable to convert key name to url: " + str);
                }
                return null;
            }).
            filter(Objects::nonNull).
            collect(Collectors.toList());
    }

    public Collection<String> retrieveConfigurationKeys() throws IOException {
        return retrieveConfigurationMap().keySet();
    }

    /**
     * Parses the configuration file from json in the form of:
     * {
     *      "https://{url to storage location}/ontology/organization/data-use.owl":
     *          {"fileName":"data-use.owl","prefix":"DUOS","ontologyType":"Organization"},
     *      "https://{url to storage location}/ontology/disease/diseases.owl":
     *          {"fileName":"diseases.owl","prefix":"DOID","ontologyType":"Disease"},
     *       "https://{url to storage location}/ontology/disease/data-use.owl":
     *          {"fileName":"data-use.owl","prefix":"DUOS","ontologyType":"Disease"}
     *  }
     *
     * @return Map of URL location to hash of file name & prefix information
     * @throws IOException
     */
    private Map<String, HashMap> retrieveConfigurationMap() throws IOException {
        String config = retrieveConfigurationFile();
        if (!config.isEmpty()) {
            return MAPPER.readerFor(Map.class).readValue(config);
        } else {
            return new HashMap<>();
        }
    }

}

