package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.google.gson.Gson;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

public class TextTranslationServiceImplTest extends AbstractTest {

    private final Logger log = Utils.getLogger(this.getClass());

    private TextTranslationServiceImpl service;

    @Mock
    private AutocompleteService autocompleteService;

    public TextTranslationServiceImplTest() {
    }

    @Before
    public void setUpClass() throws IOException {
        TermResource mockTerm = new TermResource();
        mockTerm.setId("term id");
        mockTerm.setLabel("term label");
        mockTerm.setDefinition("term definition");
        MockitoAnnotations.initMocks(this);
        Mockito.when(autocompleteService.lookupById(any())).thenReturn(Collections.singletonList(mockTerm));
        service = new TextTranslationServiceImpl(autocompleteService);
    }

    @After
    public void tearDownClass() {
    }

    @Test
    public void testTranslateSummary() {
        Gson gson = new Gson();
        DataUse dataUse = new DataUseBuilder().setGeneralUse(true).build();
        String dataUseString = gson.toJson(dataUse);
        DataUseSummary summary = service.translateDataUseSummary(dataUseString);
        assertFalse(summary.getPrimary().isEmpty());
        assertTrue(summary.getPrimary().get(0).getCode().equalsIgnoreCase("GRU"));
        Stream.of(summary.getPrimary(), summary.getSecondary()).flatMap(List::stream).forEach(e -> {
            assertFalse(e.getDescription().contains("["));
            assertFalse(e.getDescription().contains("]"));
        });
    }

    @Test
    public void testDiseaseLookup() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setDiseaseRestrictions(Collections.singletonList("term id")).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translateDataset(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("term label"));
        assertTrue(translation.contains("[DS]"));
    }

    @Test
    public void testTranslateDataset() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translateDataset(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("[GRU]"));
    }

    @Test
    public void testTranslatePurpose() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("[GRU]"));
    }

    @Test
    public void testTranslateCoverageTrue() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder()
                .setGeneralUse(true)
                .setHmbResearch(true)
                .setDiseaseRestrictions(Collections.singletonList("disease"))
                .setMethodsResearch(true)
                .setControlSetOption("Yes")
                .setCommercialUse(true)
                .setGender("Male")
                .setPediatric(true)
                .setPopulationRestrictions(Collections.singletonList("population"))
                .setAddiction(true)
                .setAggregateResearch("Yes")
                .setCloudStorage("Yes")
                .setDateRestriction("12/01/2050")
                .setEthicsApprovalRequired(true)
                .setGenomicPhenotypicData("Yes")
                .setIllegalBehavior(true)
                .setNonBiomedical(true)
                .setOther("Other")
                .setOtherRestrictions(true)
                .setGeographicalRestrictions("Geographical restriction")
                .setPopulationOriginsAncestry(true)
                .setPopulationStructure(true)
                .setPsychologicalTraits(true)
                .setRecontactMay("may")
                .setRecontactMust("must")
                .setSexualDiseases(true)
                .setStigmatizeDiseases(true)
                .setVulnerablePopulations(true)
                .setManualReview(true)
                .build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("[GRU]"));
    }

    @Test
    public void testTranslateCoverageFalse() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder()
                .setGeneralUse(false)
                .setHmbResearch(false)
                .setDiseaseRestrictions(Collections.singletonList("disease"))
                .setMethodsResearch(false)
                .setControlSetOption("No")
                .setCommercialUse(false)
                .setGender("Male")
                .setPediatric(false)
                .setPopulationRestrictions(Collections.singletonList("population"))
                .setAddiction(false)
                .setAggregateResearch("No")
                .setCloudStorage("No")
                .setDateRestriction("12/01/2050")
                .setEthicsApprovalRequired(false)
                .setGenomicPhenotypicData("No")
                .setIllegalBehavior(false)
                .setNonBiomedical(false)
                .setOther("Other")
                .setOtherRestrictions(false)
                .setGeographicalRestrictions("Geographical restriction")
                .setPopulationOriginsAncestry(false)
                .setPopulationStructure(false)
                .setPsychologicalTraits(false)
                .setRecontactMay("may")
                .setRecontactMust("must")
                .setSexualDiseases(false)
                .setStigmatizeDiseases(false)
                .setVulnerablePopulations(false)
                .setManualReview(false)
                .build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertFalse(translation.contains("[GRU]"));
    }

    @Test
    public void testInvalidDate() throws IOException {
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder()
                .setGeneralUse(true)
                .setDateRestriction("invalid date format")
                .build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translatePurpose(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("[GRU]"));
    }

}
