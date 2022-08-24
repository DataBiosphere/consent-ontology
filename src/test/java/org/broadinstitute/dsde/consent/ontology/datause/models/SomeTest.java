package org.broadinstitute.dsde.consent.ontology.datause.models;


import static junit.framework.TestCase.assertNotNull;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindswap.pellet.jena.PelletReasonerFactory;

public class SomeTest {

    private Some some;

    private UseRestriction useRestriction;

    private String property;

    @BeforeEach
    public void setUp() {
        property = "name";
        useRestriction = new Or(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(PEDIATRIC)
        );

        some = new Some(property, useRestriction);
    }

    @Test
    public void testCreateOntologicalRestriction() {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        OntClass ontClass = some.createOntologicalRestriction(model);
        assertNotNull(ontClass);
    }

    @Test
    public void testOrEqualsFalse() {
        Some newSome = new Some("test", new Everything());
        assertNotEquals(some, newSome);
    }

    @Test
    public void testOrEqualsTrue() {
        Some testObj = new Some(property, useRestriction);
        assertEquals(some, testObj);
    }

    @Test
    public void testVisitAndContinue() {
        UseRestrictionVisitor visitor = new NamedVisitor();
        assertTrue(useRestriction.visitAndContinue(visitor));
    }

}
