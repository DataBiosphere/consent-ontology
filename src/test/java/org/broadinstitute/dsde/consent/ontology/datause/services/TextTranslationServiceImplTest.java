package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

public class TextTranslationServiceImplTest extends AbstractTest {

    private final Logger log = Logger.getLogger(TextTranslationServiceImplTest.class);
    private static TextTranslationServiceImpl service;
    private static OntologyTermSearchAPI api;

    public TextTranslationServiceImplTest() {
    }

    @Before
    public void setUpClass() throws IOException, GeneralSecurityException {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        service = new TextTranslationServiceImpl(storeOntologyServiceMock);
        service.setApi(api);
    }

    @After
    public void tearDownClass() {
        service = null;
        api = null;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of translateSample method, of class TextTranslationServiceImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testTranslateSample() throws Exception {
        log.debug("translateSample");
        String restrictionStr = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://purl.obolibrary.org/obo/DOID_162\"},"
                + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/children\"},"
                + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";
        String expResult = "Samples may only be used for the purpose of studying cancer. "
                + "In addition, samples may only be used for the study of children and may not be used for commercial purposes.";
        String result = service.translateSample(restrictionStr);
        assertEquals(expResult, result);
    }

    /**
     * Test of translatePurpose method, of class TextTranslationServiceImpl.
     *
     * @throws java.lang.Exception
     */
    @Ignore
    @Test
    public void testTranslatePurpose() throws Exception {
        log.debug("translatePurpose");
        String restrictionStr = "{\"type\":\"and\",\"operands\":[{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Broad\"},"
                + "{\"type\":\"named\",\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/Non_profit\"}]}";
        String expResult = "Any sample which can be used for research at institutions in The Broad Institute.";
        String result = service.translatePurpose(restrictionStr);
        assertEquals(expResult, result);
    }

}
