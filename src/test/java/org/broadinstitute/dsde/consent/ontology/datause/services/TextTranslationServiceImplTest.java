package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TextTranslationServiceImplTest extends AbstractTest {

    private final Logger log = Logger.getLogger(TextTranslationServiceImplTest.class);

    private TextTranslationServiceImpl service;

    public TextTranslationServiceImplTest() {
    }

    @Before
    public void setUpClass() {
        service = new TextTranslationServiceImpl();
    }

    @After
    public void tearDownClass() {
    }

    @Test
    public void testTranslateDataset() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translateDataset(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("GRU"));
    }

    @Test
    public void testTranslatePurpose() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("GRU"));
    }

    @Test
    public void testTranslateCoverage() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder()
                .setGeneralUse(false)
                .setHmbResearch(true)
                .setDiseaseRestrictions(Collections.singletonList("disease"))
                .setMethodsResearch(true)
                .setControlSetOption("Yes")
                .setCommercialUse(false)
                .setGender("Male")
                .setPediatric(true)
                .setPopulationRestrictions(Collections.singletonList("population"))
                .setAddiction(false)
                .setAggregateResearch("Yes")
                .setCloudStorage("Yes")
                .setDateRestriction("12/01/2050")
                .setEthicsApprovalRequired(false)
                .setGenomicPhenotypicData("Yes")
                .setIllegalBehavior(false)
                .setNonBiomedical(false)
                .setOther("Other")
                .setOtherRestrictions(true)
                .setGeographicalRestrictions("Geographical restriction")
                .setPopulationOriginsAncestry(false)
                .setPopulationStructure(true)
                .setPsychologicalTraits(false)
                .setRecontactMay("may")
                .setRecontactMust("must")
                .setSexualDiseases(false)
                .setStigmatizeDiseases(false)
                .setVulnerablePopulations(false)
                .build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(!translation.contains("GRU"));
    }

}
