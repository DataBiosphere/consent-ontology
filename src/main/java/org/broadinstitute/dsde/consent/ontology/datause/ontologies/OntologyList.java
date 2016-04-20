package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.google.common.io.Resources;
import com.google.inject.Singleton;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mindswap.pellet.jena.PelletInfGraph;

@Singleton
public class OntologyList implements OntologyModel {

    private static Logger LOG = Logger.getLogger(OntologyList.class);

    /**
     * List of file names, each one being an ontology source file.
     */
    private final List<String> resources = new ArrayList<>();
    private OntModel baseModel;

    public OntologyList() throws IOException {
        resources.addAll(Resources.readLines(Resources.getResource("ontologies.txt"), Charset.defaultCharset()));
    }

    public OntologyList(String resource) throws IOException {
        resources.add(resource);
    }

    @Override
    public Collection<String> getResources() {
        return resources;
    }

    @Override
    public OntModel loadOntModel() throws IOException, OWLOntologyCreationException {

        OntModel umodel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        for (String resource : getResources()) {
            LOG.info(String.format("LOADING %s", resource));
            System.out.println(String.format("-----------> LOADING %s ", resource));
            try (InputStream is = Resources.getResource(resource).openStream()) {
                OntologyLoader.loadOntology(is, umodel);
//                OntologyLoader.load(is, umodel);                
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
            try (InputStream is = Resources.getResource(resource).openStream()) {
                OntologyLoader.loadOntology(is, umodel);
            }
        }
        ((PelletInfGraph) umodel.getGraph()).classify();
        return umodel;
    }

    @Override
    public OntModel getModel() throws IOException, OWLOntologyCreationException {
        if (baseModel == null) {
            baseModel = loadOntModel();
        }
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, baseModel);
        return model;
    }
}
