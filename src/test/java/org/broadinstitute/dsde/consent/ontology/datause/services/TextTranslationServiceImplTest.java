package org.broadinstitute.dsde.consent.ontology.datause.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.api.client.http.HttpResponse;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.broadinstitute.dsde.consent.ontology.AbstractTest;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class TextTranslationServiceImplTest extends AbstractTest {

    private final Logger log = Utils.getLogger(this.getClass());

    private TextTranslationServiceImpl service;

    @Mock
    private AutocompleteService autocompleteService;

    @Mock
    private GCSStore gcsStore;

    public TextTranslationServiceImplTest() {}

    @BeforeEach
    public void setUpClass() {
      openMocks(this);
      service = new TextTranslationServiceImpl(autocompleteService, gcsStore);
    }

    @Test
    public void testTranslateSummary() throws Exception {
        initializeTerm();
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
    public void testDiseaseLookup() throws Exception {
        initializeTerm();
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
    public void testTranslateDataset() throws Exception {
        initializeTerm();
        Gson gson = new Gson();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        String datasetString = gson.toJson(dataset);
        String translation = service.translateDataset(datasetString);
        log.info(translation);
        assertNotNull(translation);
        assertTrue(translation.contains("[GRU]"));
    }

    @Test
    public void testTranslateParagraphNormal() throws Exception {
      // Given
      HttpResponse response = loadTermItemsResponse();
      when(gcsStore.getStorageDocument(Mockito.anyString())).thenReturn(response);

      // When
      String mockParagraph = "GRU General research for some test with disease. This is not for profit.";
      HashMap<String, Recommendation> translationNormal = service.translateParagraph(mockParagraph);

      String gruTitle = getTitle(translationNormal, "http://purl.obolibrary.org/obo/DUO_0000042");
      String dsTitle = getTitle(translationNormal, "http://purl.obolibrary.org/obo/DUO_0000007");
      String npuTitle = getTitle(translationNormal, "http://purl.obolibrary.org/obo/DUO_0000045");

      // Then
      assertEquals(3, translationNormal.size());
      assertEquals("General Research Use", gruTitle);
      assertEquals("Disease Specific Research", dsTitle);
      assertEquals("Not for Profit Organization Use Only", npuTitle);
    }

  @Test
  public void testTranslateParagraphMixed() throws Exception {
    // Given
    HttpResponse response = loadTermItemsResponse();
    when(gcsStore.getStorageDocument(Mockito.anyString())).thenReturn(response);

    // When
    String mockParagraph = "GrU geNEraL ResEArCh fOr some tesT wiTH DiseaSE. ThiS IS noT fOr pROFIT.";
    HashMap<String, Recommendation> translation = service.translateParagraph(mockParagraph);

    String gruTitle = getTitle(translation, "http://purl.obolibrary.org/obo/DUO_0000042");
    String dsTitle = getTitle(translation, "http://purl.obolibrary.org/obo/DUO_0000007");
    String npuTitle = getTitle(translation, "http://purl.obolibrary.org/obo/DUO_0000045");

    // Then
    assertEquals(3, translation.size());
    assertEquals("General Research Use", gruTitle);
    assertEquals("Disease Specific Research", dsTitle);
    assertEquals("Not for Profit Organization Use Only", npuTitle);
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
    public void testTranslateCoverageFalse() throws Exception {
        initializeTerm();
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

  /** Helper Methods **/

  private void initializeTerm() throws Exception {
      TermResource mockTerm = new TermResource();
      mockTerm.setId("term id");
      mockTerm.setLabel("term label");
      mockTerm.setDefinition("term definition");
      when(autocompleteService.lookupById(any())).thenReturn(Collections.singletonList(mockTerm));
    }

    private HttpResponse loadTermItemsResponse() throws IOException {
      URL searchTerm = Resources.getResource("search-terms.json");
      return getMockHttpResponse(Resources.toString(searchTerm, Charset.defaultCharset()));
    }

    private String getTitle(HashMap<String, Recommendation> translation, String urlKey) {
      return translation.get(urlKey).title();
    }

}
