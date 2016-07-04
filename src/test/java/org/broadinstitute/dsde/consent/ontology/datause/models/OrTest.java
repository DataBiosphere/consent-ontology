package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.NamedVisitor;
import org.broadinstitute.dsde.consent.ontology.datause.models.visitor.UseRestrictionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class OrTest{

    private boolean result;

    private Or or;

    UseRestriction[] operands =  new UseRestriction[2];
    UseRestrictionVisitor visitor;

    @Before
    public void setUp(){
        UseRestriction and = new And(
                new Named("http://purl.obolibrary.org/obo/DOID_162"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/female"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/children")
        );
        UseRestriction or = new Or(
                new Named("http://purl.obolibrary.org/obo/DOID_0060058"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/men")
        );
        operands[0] = and;
        operands[1] = or;
        this.or = new Or(operands);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperands(){
        new Or(operands[0]);
    }

    @Test
    public void testOrEqualsFalse(){
        String testObj = "test";
        result = or.equals(testObj);
        assertFalse(result);
    }

    @Test
    public void testOrEqualsTrue(){
        Or testObj = new Or(operands);
        result = or.equals(testObj);
        assertTrue(result);
    }

    @Test
    public void testCreateOntologicalRestriction(){
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        OntClass ontClass = or.createOntologicalRestriction(model);
        assertNotNull(ontClass != null);
    }

    @Test
    public void testVisitAndContinue(){
        visitor = new NamedVisitor();
        assertTrue(or.visitAndContinue(visitor));
    }
}
