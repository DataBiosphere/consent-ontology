package org.broadinstitute.dsde.consent.ontology.service;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Predicate;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.model.MatchMessage;
import org.mindswap.pellet.PelletOptions;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;

public enum OntModelFactory implements OntologyLogger {

  INSTANCE;

  @SuppressWarnings("unused")
  public final Boolean matchPurpose(MatchMessage message) throws Exception {
    OntModel model = getOntModel(message.getUrlCollection());
    String consentId = UUID.randomUUID().toString();
    String purposeId = UUID.randomUUID().toString();
    OntClass consent = addNamedEquivalentClass(model, consentId,
        message.getMatchPair().getConsent());
    OntClass purpose = addNamedSubClass(model, purposeId, message.getMatchPair().getPurpose());

    Properties properties = new Properties();
    properties.setProperty("USE_CLASSIFICATION_MONITOR", "none");
    PelletOptions.setOptions(properties);

    // Pellet classification can have trouble with OWL.Thing or OWL.Nothing consents/purposes
    try {
      ((PelletInfGraph) model.getGraph()).classify();
    } catch (NullPointerException e) {
      logWarn("Non-fatal exception classifying: " + e.getMessage());
    }
    OntClass reclassifiedConsentClass = model.getOntClass(consentId);
    return purpose.hasSuperClass(reclassifiedConsentClass);
  }

  /**
   * Generate a model based on known resource URLs. Any group of ontology URLs can be turned into an
   * OntModel and from there, that object can be distributed out to callers
   *
   * @param resources Collection of Resources in the form of publicly accessible URLs or File
   *                  resource URLs
   * @return OntModel The OntModel
   * @throws Exception The Exception
   */
  public OntModel getOntModel(final Collection<URL> resources) throws Exception {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
    for (URL resource : resources) {
      logDebug("Loading resource: " + resource);
      try (InputStream is = resource.openStream()) {
        OWLOntology ontology;
        try {
          ontology = manager.loadOntologyFromOntologyDocument(is);
        } catch (OWLOntologyAlreadyExistsException e) {
          logWarn("Duplicate ontology exists, skipping this one: " + e.getMessage());
          break;
        }
        HashMap<String, OWLAnnotationProperty> annotationProperties = new HashMap<>();
        ontology.annotationPropertiesInSignature().forEach(property -> {
          //noinspection OptionalGetWithoutIsPresent
          annotationProperties.put(property.getIRI().getRemainder().get(), property);
        });

        OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");
        Predicate<OWLAnnotationProperty> hasDeprecated = op -> op
            .equals(deprecated);
        ontology.classesInSignature().forEach(
            owlClass -> {
              boolean anyDeprecated = owlClass.annotationPropertiesInSignature()
                  .anyMatch(hasDeprecated);
              if (anyDeprecated) {
                logInfo("Class has deprecated terms: " + owlClass);
              }
              // Do not load deprecated classes.
              if (!anyDeprecated) {
                String id = owlClass.toStringID();
                OntClass ontClass = model.createClass(id);
                EntitySearcher.getSuperClasses(owlClass, ontology).forEach(
                    expr -> {
                      if (expr instanceof OWLClass) {
                        OWLClass cexpr = (OWLClass) expr;
                        // this ignores some obvious restriction / intersection classes.
                        OntClass superClass = model.createClass(cexpr.toStringID());
                        ontClass.addSuperClass(superClass);
                      }
                    });
              }
            });
      }
    }
    return model;
  }

  private OntClass addNamedEquivalentClass(OntModel model, String name,
      UseRestriction restriction) {
    OntClass cls = model.createClass(name);
    cls.addEquivalentClass(restriction.createOntologicalRestriction(model));
    return cls;
  }

  private OntClass addNamedSubClass(OntModel model, String name, UseRestriction restriction) {
    OntClass cls = model.createClass(name);
    cls.addSuperClass(restriction.createOntologicalRestriction(model));
    return cls;
  }

}
