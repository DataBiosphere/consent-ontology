package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OnlyTest {

    private Only only;
    private boolean result;
    private String property;

    private UseRestriction useRestriction;


    @Before
    public void setUp() {
        UseRestriction and = new And(
                new Named("http://purl.obolibrary.org/obo/DOID_162"),
                new Named("http://www.broadinstitute.org/ontologies/DUOS/female"),
                new Named("http://www.broadinstitute.org/ontologies/DUOS/men")
        );
        property = "Test";
        useRestriction = and;
        only = new Only(property, useRestriction);
    }

    @Test
    public void testCreateOntologicalRestriction(){
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        OntClass ontClass = only.createOntologicalRestriction(model);
        assertNotNull(ontClass != null);
    }

    @Test
    public void testOrEqualsFalse(){
        String testObj = "test";
        result = only.equals(testObj);
        assertFalse(result);
    }

    @Test
    public void testOrEqualsTrue(){
        Only testObj = new Only(property, useRestriction);
        result = only.equals(testObj);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        String type = "only";
        int value = Objects.hashCode(type, property, useRestriction);
        assertNotNull(value);
    }

    @Test
    public void testVisitAndContinue(){
        UseRestrictionVisitor visitor = new NamedVisitor();
        assertTrue(useRestriction.visitAndContinue(visitor));
    }
}
