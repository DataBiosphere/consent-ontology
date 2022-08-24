package org.broadinstitute.dsde.consent.ontology.datause.models;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.MALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.Assert.assertThrows;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindswap.pellet.jena.PelletReasonerFactory;

public class OrTest {

    private final UseRestriction[] operands = new UseRestriction[2];
    private UseRestrictionVisitor visitor;
    private Or or;

    @BeforeEach
    public void setUp() {
        UseRestriction and = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(PEDIATRIC)
        );
        UseRestriction or = new Or(
            new Named("http://purl.obolibrary.org/obo/DOID_0060058"),
            new Named(MALE)
        );
        operands[0] = and;
        operands[1] = or;
        this.or = new Or(operands);
    }

    @Test
    public void testOperands() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Or(operands[0]);
        });
    }

    @Test
    public void testOrEqualsFalse() {
        Or newOr = new Or(new Everything(), new Nothing());
        Assertions.assertNotEquals(or, newOr);
    }

    @Test
    public void testOrEqualsTrue() {
        Or testObj = new Or(operands);
        Assertions.assertEquals(or, testObj);
    }

    @Test
    public void testCreateOntologicalRestriction() {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        OntClass ontClass = or.createOntologicalRestriction(model);
        Assertions.assertNotNull(ontClass);
    }

    @Test
    public void testVisitAndContinue() {
        visitor = new NamedVisitor();
        Assertions.assertTrue(or.visitAndContinue(visitor));
    }
}
