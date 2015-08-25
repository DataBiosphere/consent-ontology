package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.models.ResearchPurpose;
import org.broadinstitute.dsde.consent.ontology.datause.models.Consent;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
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

    public ResearchPurposeMatch(String ontologyResource) throws IOException, OWLOntologyCreationException {
        this(new OntologyList(ontologyResource));
    }

    public ResearchPurposeMatch(OntologyList ontologies) throws IOException, OWLOntologyCreationException {
        this.ontologyList = ontologies;
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

    public Boolean matchPurpose(ResearchPurpose purpose, Consent consent) {
        Boolean match = false;
        UseRestriction query = purpose.getPurpose();
        try {
            OntModel model = ontologyList.loadOntModel();
            addNamedEquivalentClass(model, consent.getId(), consent.getRestriction());

            String randomId = UUID.randomUUID().toString();
            OntClass rpClass = addNamedSubClass(model, randomId, query);
            ((PelletInfGraph) model.getGraph()).classify();

            OntClass sampleSetClass = model.getOntClass(consent.getId());
            match = rpClass.hasSuperClass(sampleSetClass);
        } catch (IOException | OWLOntologyCreationException e) {
            e.printStackTrace(System.err);
        }

        return match;
    }

}
