package org.broadinstitute.dsde.consent.ontology.datause.models;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ResearchPurposeTest extends ResearchPurpose {

    @Test
    public void testNullConstructor() {
        assertNull(getId());
        assertNull(getPurpose());
    }

    @Test
    public void testEqualsFalseInstanceOf() {
        String test = "test";
        boolean result = equals(test);
        assertFalse(result);
    }

    @Test
    public void testEqualsInstanceOf() {
        ResearchPurpose researchPurpose = new ResearchPurpose();
        boolean result = equals(researchPurpose);
        assertTrue(result);
    }

    @Test
    public void testPropertiesAssignment() throws IOException {
        setId("Id Test");
        assertNotNull(getId());
        UseRestriction and = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named(FEMALE),
            new Named(PEDIATRIC)
        );
        setPurpose(and);
        assertNotNull(getPurpose());
    }
}
