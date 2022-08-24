package org.broadinstitute.dsde.consent.ontology.service;


import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.NON_PROFIT;
import static org.broadinstitute.dsde.consent.ontology.datause.builder.UseRestrictionBuilderSupport.PEDIATRIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.model.ValidationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class UseRestrictionValidatorTest extends AbstractTest {

    private static UseRestrictionValidator service;
    private final Logger log = Utils.getLogger(this.getClass());

    @BeforeAll
    public static void setUpClass() throws Exception {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        OntologyTermSearchAPI api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        service = new UseRestrictionValidator(api);
    }

    /**
     * Test of validateUseRestriction method, of class
     * UseRestrictionValidator.
     */
    @Test
    public void testValidateUseRestrictionValid() throws Exception {
        log.debug("validateUseRestriction");
        String validRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
            + "{\"type\":\"named\",\"name\":\"" + PEDIATRIC + "\"},"
            + "{\"type\":\"named\",\"name\":\"" + NON_PROFIT + "\"}]}";
        ValidationResponse result = service.validateUseRestriction(validRestriction);
        assertTrue(result.isValid());
    }

    @Test
    public void testValidateUseRestrictionFail() throws Exception {
        log.debug("validateUseRestriction");
        String invalidRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_X162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DUOS/choldren\"},"
            + "{\"type\":\"named\",\"name\":\"" + NON_PROFIT + "\"}]}";
        ValidationResponse result = service.validateUseRestriction(invalidRestriction);
        assertFalse(result.isValid());
    }

    @Test
    public void testValidateUseRestrictionInvalidTerm() throws Exception {
        String invalidRestriction = "{\"type\":\"named\",\"name\": \"http://purl.obolibrary.org/obo/BLURB\"}";
        ValidationResponse result = service.validateUseRestriction(invalidRestriction);
        assertFalse(result.isValid());
        assertEquals("Term not found: http://purl.obolibrary.org/obo/BLURB",
            ((ArrayList) result.getErrors()).get(0));
    }

    @Test
    public void testValidateUseRestrictionErrorMessage() throws Exception {
        String invalidRestriction = "{\"type\":\"named\",\"weird\": \"http://purl.obolibrary.org/obo/BLURB\"}";
        ValidationResponse result = service.validateUseRestriction(invalidRestriction);
        assertFalse(result.isValid());
        assertEquals(
            "Could not resolve the following keys: weird into a subtype of type, operands, operand, name",
            ((ArrayList) result.getErrors()).get(0));
    }
}
