package org.broadinstitute.dsde.consent.ontology.service.validate;

import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class UseRestrictionValidateImplTest extends AbstractTest {

    private static UseRestrictionValidateImpl service;
    private static OntologyModel ontologyList;
    private static OntologyTermSearchAPI api;

    private static String validRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/children\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";

    private static String invalidRestriction = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_X162\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/choldren\"},"
            + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";

    @BeforeClass
    public static void setUpClass() throws Exception {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        ontologyList = new OntologyList(storeOntologyServiceMock);
        api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        ((LuceneOntologyTermSearchAPI) api).setOntologyList(ontologyList);

        service = new UseRestrictionValidateImpl();
        service.setOntologyList(api);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test of validateUseRestriction method, of class
     * UseRestrictionValidateImpl.
     */
    @Test
    public void testValidateUseRestrictionValid() throws Exception {
        System.out.println("validateUseRestriction");
        String useRestriction = validRestriction;
        ValidateResponse expResult = null;
        ValidateResponse result = service.validateUseRestriction(useRestriction);
        Assert.assertEquals(true, result.isValid());
    }

    @Test
    public void testValidateUseRestrictionFail() throws Exception {
        System.out.println("validateUseRestriction");
        String useRestriction = invalidRestriction;
        ValidateResponse expResult = null;
        ValidateResponse result = service.validateUseRestriction(useRestriction);
        Assert.assertEquals(false, result.isValid());
    }
}
