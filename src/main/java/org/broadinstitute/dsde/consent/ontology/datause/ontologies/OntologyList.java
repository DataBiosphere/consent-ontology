/**
 * Copyright 2014 Genome Bridge LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.google.common.io.Resources;
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

public class OntologyList {

    private static Logger LOG = Logger.getLogger(OntologyList.class);

    /**
     * List of file names, each one being an ontology source file.
     */
    private List<String> resources = new ArrayList<>();

    public OntologyList() throws IOException {
        resources.addAll(
                Resources.readLines(
                        Resources.getResource("ontologies.txt"),
                        Charset.defaultCharset())
        );
    }

    public OntologyList(String resource) throws IOException {
        resources.add(resource);
    }

    public Collection<String> getResources() {
        return resources;
    }

    private long countBytes(String resource) throws IOException {
        long total = 0;
        try (InputStream is = Resources.getResource(resource).openStream()) {
            byte[] buffer = new byte[1024*1024];
            int read;
            while((read = is.read(buffer)) != -1) {
                total += read;
            }
        }
        return total;
    }

    public OntModel loadOntModel() throws IOException, OWLOntologyCreationException {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        for(String resource : getResources()) {
            long bytes = countBytes(resource);
            LOG.info(String.format("LOADING %s (%d)", resource, bytes));
            System.out.println(String.format("LOADING %s (%d)", resource, bytes));
            try (InputStream is = Resources.getResource(resource).openStream()) {
                OntologyLoader.loadOntology(is, model);
            }
        }
        return model;
    }

    public OntModel loadBaseOntModel() throws IOException, OWLOntologyCreationException {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        for(String resource : getResources()) {
            long bytes = countBytes(resource);
            LOG.info(String.format("LOADING %s (%d)", resource, bytes));
            System.out.println(String.format("LOADING %s (%d)", resource, bytes));
            try (InputStream is = Resources.getResource(resource).openStream()) {
                OntologyLoader.loadOntology(is, model);
            }
        }
        return model;
    }
}
