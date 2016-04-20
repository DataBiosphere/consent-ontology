package org.broadinstitute.dsde.consent.ontology.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.UUID;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.resources.MatchDTO;

/**
 * TODO: Migrate ontology uses to make use of this actor
 */
public class OntologyMatchingActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public OntologyMatchingActor() {
        receive(
            ReceiveBuilder.match(MatchDTO.class, msg -> {
                    log.debug("Replying to message: {}", msg);
                    sender().tell(matchPurpose(msg.getPurpose(), msg.getConsent(), msg.getOntologyModel()), self());
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

    public Boolean matchPurpose(UseRestriction purpose, UseRestriction consent, OntologyModel ontologyList) {
        long start = System.currentTimeMillis();
        String consentId = UUID.randomUUID().toString();
        String purposeId = UUID.randomUUID().toString();
        Boolean match = false;
        try {
            OntModel model = ontologyList.getModel();
            addNamedEquivalentClass(model, consentId, consent);

            OntClass rpClass = addNamedSubClass(model, purposeId, purpose);
            ((PelletInfGraph) model.getGraph()).classify();

            OntClass sampleSetClass = model.getOntClass(consentId);
            match = rpClass.hasSuperClass(sampleSetClass);
        } catch (IOException | OWLOntologyCreationException e) {
            log.error(e, "White match: " + consent + " - " + purpose);
        }
        log.debug(String.format("Match = %b\n%s\n%s\nduration: %d milliseconds",
                match, consent, purpose, (System.currentTimeMillis() - start)));
        return match;
    }

}
