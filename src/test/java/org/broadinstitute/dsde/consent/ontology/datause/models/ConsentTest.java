package org.broadinstitute.dsde.consent.ontology.datause.models;

import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.FEMALE;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ConsentTest extends Consent {

    @Test
    public void testObjectInstanceOf() {
        Consent consent = new Consent();
        boolean result = equals(consent);
        assertTrue(result);
    }

    @Test
    public void testObjectNotInstanceOf() {
        String test = "test";
        boolean result = equals(test);
        assertFalse(result);
    }

    @Test
    public void testNullConstructor() {
        assertNull(getId());
        assertNull(getRestriction());
    }

    @Test
    public void testHashCode() {
        assertNotNull(hashCode());
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
        setRestriction(and);
        assertNotNull(getRestriction());
    }
}
