package org.broadinstitute.dsde.consent.ontology.datause.models;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


public class ConsentTest extends Consent {

    @Test
    public void testObjetInstanceOf(){
        Consent consent = new Consent();
        boolean result = equals(consent);
        Assert.assertTrue(result);
    }

    @Test
    public void testObjectNotInstanceOf(){
        String test = "test";
        boolean result = equals(test);
        Assert.assertFalse(result);
    }

    @Test
    public void testNullConstructor(){
        assertNull(getId());
        assertNull(getRestriction());
    }

    @Test
    public void testHashCode(){
        assertNotNull(hashCode());
    }

    @Test
    public void testPropertiesAssignment() throws IOException {
        setId("Id Test");
        assertNotNull(getId());
        UseRestriction and = new And(
                new Named("http://purl.obolibrary.org/obo/DOID_162"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/female"),
                new Named("http://www.broadinstitute.org/ontologies/DURPO/children")
        );
        setRestriction(and);
        assertNotNull(getRestriction());
    }
}
