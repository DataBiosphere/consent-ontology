package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.api.client.http.HttpResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.mindswap.pellet.jena.PelletInfGraph;


@Singleton
public class OntologyList implements OntologyModel {

    private static Logger LOG = Logger.getLogger(OntologyList.class);

    /**
     * List of file names, each one being an ontology source file.
     */
    private  List<String> resources = new ArrayList<>();
    private OntModel model;
    private StoreOntologyService storeOntologyService;



    @Inject
    public OntologyList(StoreOntologyService storeOntologyService) throws IOException {
        LOG.info(String.format("LOADING ontology configuration files."));
        String configurationFileString = storeOntologyService.retrieveConfigurationFile();
        Map<String, HashMap> map = parseAsMap(configurationFileString);
        List list = new ArrayList<String>();
        list.addAll(map.keySet());
        this.storeOntologyService = storeOntologyService;
        this.resources = list;
    }
    private static final ObjectMapper mapper = new ObjectMapper();

    //@TODO; should be removed to CloudStore
    private Map<String, HashMap> parseAsMap(String str) throws IOException {
        ObjectReader reader = mapper.reader(Map.class);
        return reader.readValue(str);
    }


    @Override
    public Collection<String> getResources() {
        return resources;
    }

    @Override
    public OntModel loadOntModel() throws IOException, OWLOntologyCreationException {
        OntModel umodel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        for (String resource : getResources()) {
            HttpResponse response = storeOntologyService.retrieveFile(resource);
            LOG.info(String.format("LOADING %s", resource));
            try (InputStream is = response.getContent()) {
                OntologyLoader.loadOntology(is, umodel);
            }
        }
        ((PelletInfGraph) umodel.getGraph()).classify();
        return umodel;
    }

    @Override
    public OntModel loadBaseOntModel() throws IOException, OWLOntologyCreationException {
        OntModel umodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        for (String resource : getResources()) {
            LOG.info(String.format("LOADING %s", resource));
            HttpResponse response = storeOntologyService.retrieveFile(resource);
            try (InputStream is = response.getContent()) {
                OntologyLoader.loadOntology(is, umodel);
            }
        }
        ((PelletInfGraph) umodel.getGraph()).classify();
        return umodel;
    }

    @Override
    public OntModel getModel() throws IOException, OWLOntologyCreationException {
        if (model == null) {
            model = loadOntModel();
        }
        return model;
    }
}
