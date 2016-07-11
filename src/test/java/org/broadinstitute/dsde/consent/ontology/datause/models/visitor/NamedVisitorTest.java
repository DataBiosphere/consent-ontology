package org.broadinstitute.dsde.consent.ontology.datause.models.visitor;

import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

public class NamedVisitorTest {

    NamedVisitor namedVisitor;
    UseRestriction name;
    private Collection<String> namedClasses = new ArrayList<>();

    @Before
    public void setUp() {
        UseRestriction and = new And(
                new Named("http://purl.obolibrary.org/obo/DOID_162"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/female"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/men")
        );
        name = and;
        namedVisitor = new NamedVisitor();
    }

    @Test
    public void testVisitTrue(){
        assertTrue(namedVisitor.visit(name));
    }
    
}