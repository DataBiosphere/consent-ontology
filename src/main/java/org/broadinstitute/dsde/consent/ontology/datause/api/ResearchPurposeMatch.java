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
package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.models.ResearchPurpose;
import org.broadinstitute.dsde.consent.ontology.datause.models.SampleSet;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ClassLoaderResourceLoader;
import org.broadinstitute.dsde.consent.ontology.datause.utils.ResourceLoader;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.UUID;

// this routine borrows from both JDBCSampleSetAPI and JDBCResearchPurposeAPI from the data use prototype
// but isolates out the JDBC datasource dependencies that don't really help us here

// the attempt to optimize the performance by using a baseModel to hold the ontologies in memory breaks concurrency..
// keep the code here for now but commented out... (so I don't forget how to do it)

public class ResearchPurposeMatch {

    private final OntologyList ontologyList;
//    private final OntModel baseModel;

    public ResearchPurposeMatch() throws IOException, OWLOntologyCreationException {
        this("ontologies.txt", new ClassLoaderResourceLoader(ClassLoader.getSystemClassLoader()));
    }

    public ResearchPurposeMatch(String ontologyListName, ResourceLoader loader) throws IOException, OWLOntologyCreationException {
        this(new OntologyList(ontologyListName, loader));
    }

    public ResearchPurposeMatch(OntologyList ontologies) throws IOException, OWLOntologyCreationException {
        this.ontologyList = ontologies;
//        this.baseModel = ontologies.loadBaseOntModel();
    }

    private OntClass addNamedEquivalentClass(OntModel model, String name, UseRestriction restriction) {
        OntClass cls = model.createClass(name);
        cls.addEquivalentClass(restriction.createOntologicalRestriction(model));
        return cls;
    }

    private OntClass addNamedSubClass(OntModel model, String name, UseRestriction restriction) {
        OntClass cls = model.createClass(name);
        cls.addSuperClass(restriction.createOntologicalRestriction(model));
        return cls;
    }

    public Boolean[] matchPurpose(ResearchPurpose purpose, SampleSet[] consents) {

        Boolean[] matches = new Boolean[consents.length];
        UseRestriction query = purpose.getPurpose();

        try {
            OntModel model = ontologyList.loadOntModel();
//            OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, baseModel);

            for(SampleSet consent : consents) {
                addNamedEquivalentClass(model, consent.getId(), consent.getRestriction());
            }

            String randomId = UUID.randomUUID().toString();
            OntClass rpClass = addNamedSubClass(model, randomId, query);
            ((PelletInfGraph) model.getGraph()).classify();

            Integer i = 0;
            for(SampleSet consent : consents) {
                OntClass sampleSetClass = model.getOntClass(consent.getId());
                matches[i++] = rpClass.hasSuperClass(sampleSetClass);
            }

        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace(System.err);
        }

        return matches;
    }
}
