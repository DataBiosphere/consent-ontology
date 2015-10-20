package org.broadinstitute.dsde.consent.ontology.datause.api;

import com.google.inject.Inject;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.models.ResearchPurpose;
import org.broadinstitute.dsde.consent.ontology.datause.models.Consent;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.UUID;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;

// this routine borrows from both JDBCSampleSetAPI and JDBCResearchPurposeAPI from the data use prototype
// but isolates out the JDBC datasource dependencies that don't really help us here

// the attempt to optimize the performance by using a baseModel to hold the ontologies in memory breaks concurrency..
// keep the code here for now but commented out... (so I don't forget how to do it)

/**
 * TODO: Migrate the use of this class to instead use OntologyMatchingActor
 */
public class ResearchPurposeMatch {

    private OntologyModel ontologyList = null;

    public ResearchPurposeMatch() {
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
            OntModel model = ontologyList.getModel();
            addNamedEquivalentClass(model, consent.getId(), consent.getRestriction());

            String randomId = UUID.randomUUID().toString();
            OntClass rpClass = addNamedSubClass(model, randomId, query);
//            ((PelletInfGraph) model.getGraph()).classify();

            OntClass sampleSetClass = model.getOntClass(consent.getId());
            match = rpClass.hasSuperClass(sampleSetClass);
        } catch (IOException | OWLOntologyCreationException e) {
            e.printStackTrace(System.err);
        }

        return match;
    }

    @Inject
    public void setOntologyList(OntologyModel ontologyList) {
        this.ontologyList = ontologyList;
    }

    
}
