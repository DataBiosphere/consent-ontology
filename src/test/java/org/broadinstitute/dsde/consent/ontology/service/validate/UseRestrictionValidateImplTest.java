package org.broadinstitute.dsde.consent.ontology.service.validate;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UseRestrictionValidateImplTest extends AbstractTest {

    private final Logger log = Logger.getLogger(UseRestrictionValidateImplTest.class);

    private static UseRestrictionValidateImpl service;

    @BeforeClass
    public static void setUpClass() throws Exception {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        OntologyTermSearchAPI api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        service = new UseRestrictionValidateImpl();
        service.setOntologySearchTermAPI(api);
    }

    /**
     * Test of validateUseRestriction method, of class
     * UseRestrictionValidateImpl.
     */
    @Test
    public void testValidateUseRestrictionValid() throws Exception {
        log.debug("validateUseRestriction");
        String validRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/children\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";
        ValidateResponse result = service.validateUseRestriction(validRestriction);
        Assert.assertEquals(true, result.isValid());
    }

    @Test
    public void testValidateUseRestrictionFail() throws Exception {
        log.debug("validateUseRestriction");
        String invalidRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_X162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/choldren\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";
        ValidateResponse result = service.validateUseRestriction(invalidRestriction);
        Assert.assertEquals(false, result.isValid());
    }
}
