package org.broadinstitute.dsde.consent.ontology.datause.models.visitor;

import static junit.framework.TestCase.assertTrue;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.MALE;

import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NamedVisitorTest {

    private NamedVisitor namedVisitor;
    private UseRestriction name;

    @BeforeEach
    public void setUp() {
        name = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(MALE)
        );
        namedVisitor = new NamedVisitor();
    }

    @Test
    public void testVisitTrue() {
        assertTrue(namedVisitor.visit(name));
    }

}