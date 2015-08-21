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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ClassLoaderResourceLoader;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ResourceLoader;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class OntologyList {

    private static Logger LOG = Logger.getLogger(OntologyList.class);

    private ArrayList<String> resources;
    private ResourceLoader loader;

    public OntologyList() throws IOException {
        this("ontologies.txt", new ClassLoaderResourceLoader(ClassLoader.getSystemClassLoader()));
    }

    public OntologyList(Collection<String> resources, ResourceLoader loader) throws IOException {
        this.resources = new ArrayList<>(resources);
        this.loader = loader;
    }

    public OntologyList(String resource, ResourceLoader loader) throws IOException {
        resources = new ArrayList<String>();
        this.loader = loader;

        InputStream is = loader.loadResource(resource);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String line = null;
        while((line = br.readLine()) != null) {
            resources.add(line);
        }

        br.close();
    }

    public Collection<String> getResources() {
        return resources;
    }

    private long countBytes(String resource) throws IOException {
        InputStream is = loader.loadResource(resource);
        byte[] buffer = new byte[1024*1024];
        long total = 0;
        int read = 0;
        while((read = is.read(buffer)) != -1) {
            total += read;
        }
        is.close();
        return total;
    }

    public OntModel loadOntModel() throws IOException, OWLOntologyCreationException {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        assert model != null : "Model shouldn't be null";

        for(String resource : getResources()) {
            long bytes = countBytes(resource);
            LOG.info(String.format("LOADING %s (%d)", resource, bytes));
            System.out.println(String.format("LOADING %s (%d)", resource, bytes));

            InputStream is = loader.loadResource(resource);
            OntologyLoader.loadOntology(is, model);
            is.close();
        }

        return model;
    }


    public OntModel loadBaseOntModel() throws IOException, OWLOntologyCreationException {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        assert model != null : "Model shouldn't be null";

        for(String resource : getResources()) {
            long bytes = countBytes(resource);
            LOG.info(String.format("LOADING %s (%d)", resource, bytes));
            System.out.println(String.format("LOADING %s (%d)", resource, bytes));

            InputStream is = loader.loadResource(resource);
            OntologyLoader.loadOntology(is, model);
            is.close();
        }

        return model;
    }
}
