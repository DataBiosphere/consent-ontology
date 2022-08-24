package org.broadinstitute.dsde.consent.ontology.datause.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class AndTest {

    @Test
    public void testEquals(){
        UseRestriction and1 = new And(
            new Named("one"),
            new Named("two"),
            new Named("three")
        );
        UseRestriction and2 = new And(
            new Named("three"),
            new Named("two"),
            new Named("one")
        );
        UseRestriction and3 = new And(
            new Named("one"),
            new Named("two"),
            new Named("two")
        );

        // Tests that order is not used in equals comparison
        assertEquals(and1, and2);
        assertEquals(and2, and1);

        // Tests that same number of elements, first having all elements in the second,
        // but the second doesn't have all elements of the first, will fail
        assertNotEquals(and1, and3);
        assertNotEquals(and2, and3);

        // For completeness' sake, check the inverse
        assertNotEquals(and3, and1);
        assertNotEquals(and3, and2);

    }

}
