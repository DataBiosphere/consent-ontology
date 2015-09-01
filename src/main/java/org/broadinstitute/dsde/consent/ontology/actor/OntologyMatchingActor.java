package org.broadinstitute.dsde.consent.ontology.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.resources.MatchPair;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.UUID;

/**
 * TODO: Migrate ontology uses to make use of this actor
 */
public class OntologyMatchingActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public OntologyMatchingActor() {
        receive(
            ReceiveBuilder
                .match(MatchPair.class, msg -> {
                    log.debug("Replying to message: {}", msg);
                    sender().tell(matchPurpose(msg.purpose, msg.consent), self());
                })
                .build()
        );
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

    public Boolean matchPurpose(UseRestriction purpose, UseRestriction consent) {
        String consentId = UUID.randomUUID().toString();
        Boolean match = false;
        try {
            OntModel model = new OntologyList().loadOntModel();
            addNamedEquivalentClass(model, consentId, consent);

            String randomId = UUID.randomUUID().toString();
            OntClass rpClass = addNamedSubClass(model, randomId, purpose);
            ((PelletInfGraph) model.getGraph()).classify();

            OntClass sampleSetClass = model.getOntClass(consentId);
            match = rpClass.hasSuperClass(sampleSetClass);
        } catch (IOException | OWLOntologyCreationException e) {
            e.printStackTrace(System.err);
        }
        return match;
    }

}
