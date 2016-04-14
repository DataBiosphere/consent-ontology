package org.broadinstitute.dsde.consent.ontology.datause.services;

import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.datause.api.LuceneOntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.api.OntologyTermSearchAPI;
import org.broadinstitute.dsde.consent.ontology.datause.models.And;
import org.broadinstitute.dsde.consent.ontology.datause.models.Named;
import org.broadinstitute.dsde.consent.ontology.datause.models.Or;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyModel;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertTrue;

public class HtmlTranslatorTest extends AbstractTest{

    private static HtmlTranslator service;
    private static OntologyModel ontologyList;
    private static OntologyTermSearchAPI api;

    @BeforeClass
    public static void setUpClass() throws IOException, GeneralSecurityException {
        StoreOntologyService storeOntologyServiceMock = getStorageServiceMock();
        ontologyList = new OntologyList(storeOntologyServiceMock);
        api = new LuceneOntologyTermSearchAPI(storeOntologyServiceMock);
        ((LuceneOntologyTermSearchAPI)api).setOntologyList(ontologyList);
        service = new HtmlTranslator();
        service.setApi(api);
    }

    @AfterClass
    public static void tearDownClass() {
        service = null;
        ontologyList = null;
        api = null;
    }

    @Test
    public void testTranslateSample() throws Exception {
        UseRestriction and = new And(
            new Named("http://purl.obolibrary.org/obo/DOID_162"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/female"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/children")
        );

        UseRestriction or = new Or(
            new Named("http://purl.obolibrary.org/obo/DOID_6741"),
            new Named("http://www.broadinstitute.org/ontologies/DURPO/Non_profit")
        );

        UseRestriction andOrEd = new And(and, or);

        String translated = service.translateSample(andOrEd.toString());
//        System.out.println(translated);
        assertTrue(translated.contains("<ul>"));
        assertTrue(translated.contains("</ul>"));
        assertTrue(translated.contains("<li>"));
        assertTrue(translated.contains("</li>"));
    }

}
