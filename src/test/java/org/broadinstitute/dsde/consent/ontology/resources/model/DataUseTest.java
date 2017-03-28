package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.DropwizardClientRule;
import org.broadinstitute.dsde.consent.ontology.resources.DataUseResource;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.parboiled.common.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class DataUseTest {

    /**
     * This spins up a server so we can ensure that SchemaLoader will be able to resolve `data-use.json` URL
     */
    @ClassRule
    public static final DropwizardClientRule RULE = new DropwizardClientRule(new DataUseResource());

    static ObjectMapper MAPPER;
    static String DU_CONTENT;
    static JSONObject RAW_SCHEMA;
    static SchemaLoader LOADER;
    static Schema DU_SCHEMA;

    @BeforeClass
    public static void setUp() {
        MAPPER = new ObjectMapper();
        DU_CONTENT = FileUtils.readAllTextFromResource("data-use.json");
        RAW_SCHEMA = new JSONObject(DU_CONTENT);
        LOADER = SchemaLoader.builder().schemaJson(RAW_SCHEMA).
            resolutionScope(RULE.baseUri() + "/schemas/data-use").build();
        DU_SCHEMA = LOADER.load().build();
    }

    @Test
    public void validateDataUseJsonFile() throws ValidationException, IOException {
        try (InputStream schemaStream = new URL("http://json-schema.org/draft-04/schema").openStream()) {
            String schemaString = FileUtils.readAllText(schemaStream);
            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaString));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(DU_CONTENT)); // throws a ValidationException if this object is invalid
        }
    }

    private void assertInvalidJson(String snippet) {
        boolean thrown = false;
        try {
            DU_SCHEMA.validate(new JSONObject(snippet));
        } catch (ValidationException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    private void assertValidJson(String snippet) {
        DU_SCHEMA.validate(new JSONObject(snippet));
        assertTrue(true);
    }

    @Test
    public void generalUse() throws Exception {
        String json = "{ \"generalUse\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getGeneralUse());
        assertValidJson(json);
        assertInvalidJson("{ \"generalUse\": \"string\" }");
    }

    @Test
    public void hmbResearch() throws Exception {
        String json = "{ \"hmbResearch\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getHmbResearch());
        assertValidJson(json);
        assertInvalidJson("{ \"hmbResearch\": \"string\" }");
    }

    @Test
    public void diseaseRestrictions() throws Exception {
        String json = "{ \"diseaseRestrictions\": [\"one\", \"two\"] }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getDiseaseRestrictions().size() == 2);
        assertValidJson(json);
        assertInvalidJson("{ \"diseaseRestrictions\": \"string\" }");
    }

    @Test
    public void populationOriginsAncestry() throws Exception {
        String json = "{ \"populationOriginsAncestry\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getPopulationOriginsAncestry());
        assertValidJson(json);
        assertInvalidJson("{ \"populationOriginsAncestry\": \"string\" }");
    }

    @Test
    public void commercialUseExcluded() throws Exception {
        String json = "{ \"commercialUseExcluded\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getCommercialUseExcluded());
        assertValidJson(json);
        assertInvalidJson("{ \"commercialUseExcluded\": \"string\" }");
    }

    @Test
    public void methodsResearchExcluded() throws Exception {
        String json = "{ \"methodsResearchExcluded\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getMethodsResearchExcluded());
        assertValidJson(json);
        assertInvalidJson("{ \"methodsResearchExcluded\": \"string\" }");
    }

    @Test
    public void aggregateResearchResponse() throws Exception {
        String json = "{ \"aggregateResearchResponse\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getAggregateResearchResponse().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"aggregateResearchResponse\": \"string\" }");
    }

    @Test
    public void gender() throws Exception {
        String json = "{ \"gender\": \"Male\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getGender().equals("Male"));
        assertValidJson(json);
        assertInvalidJson("{ \"gender\": \"string\" }");
    }

    @Test
    public void controlSetExcluded() throws Exception {
        String json = "{ \"controlSetExcluded\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getControlSetExcluded());
        assertValidJson(json);
        assertInvalidJson("{ \"controlSetExcluded\": \"string\" }");
    }

    @Test
    public void controlSetOption() throws Exception {
        String json = "{ \"controlSetOption\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getControlSetOption().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"controlSetOption\": \"string\" }");
    }

    @Test
    public void populationRestrictions() throws Exception {
        String json = "{ \"populationRestrictions\": [\"one\", \"two\"] }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getPopulationRestrictions().size() == 2);
        assertValidJson(json);
        assertInvalidJson("{ \"populationRestrictions\": \"string\" }");
    }

    @Test
    public void pediatricLimited() throws Exception {
        String json = "{ \"pediatricLimited\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getPediatricLimited());
        assertValidJson(json);
        assertInvalidJson("{ \"pediatricLimited\": \"string\" }");
    }

    @Test
    public void dateRestriction() throws Exception {
        String json = "{ \"dateRestriction\": \"date\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getDateRestriction().equals("date"));
        assertValidJson(json);
        assertInvalidJson("{ \"dateRestriction\": true }");
    }

    @Test
    public void recontactingDataSubjects() throws Exception {
        String json = "{ \"recontactingDataSubjects\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getRecontactingDataSubjects());
        assertValidJson(json);
        assertInvalidJson("{ \"recontactingDataSubjects\": \"string\" }");
    }

    @Test
    public void recontactMay() throws Exception {
        String json = "{ \"recontactMay\": \"may\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getRecontactMay().equals("may"));
        assertValidJson(json);
        assertInvalidJson("{ \"recontactMay\": true }");
    }

    @Test
    public void recontactMust() throws Exception {
        String json = "{ \"recontactMust\": \"must\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getRecontactMust().equals("must"));
        assertValidJson(json);
        assertInvalidJson("{ \"recontactMust\": true }");
    }

    @Test
    public void genomicPhenotypicData() throws Exception {
        String json = "{ \"genomicPhenotypicData\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getGenomicPhenotypicData().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"genomicPhenotypicData\": true }");
    }

    @Test
    public void otherRestrictions() throws Exception {
        String json = "{ \"otherRestrictions\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getOtherRestrictions());
        assertValidJson(json);
        assertInvalidJson("{ \"otherRestrictions\": \"string\" }");
    }

    @Test
    public void cloudStorage() throws Exception {
        String json = "{ \"cloudStorage\": \"Yes\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getCloudStorage().equals("Yes"));
        assertValidJson(json);
        assertInvalidJson("{ \"cloudStorage\": \"string\" }");
    }

    @Test
    public void geographicalRestrictions() throws Exception {
        String json = "{ \"geographicalRestrictions\": \"geo\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getGeographicalRestrictions().equals("geo"));
        assertValidJson(json);
        assertInvalidJson("{ \"geographicalRestrictions\": true }");
    }

    @Test
    public void other() throws Exception {
        String json = "{ \"other\": \"other\" }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getOther().equals("other"));
        assertValidJson(json);
        assertInvalidJson("{ \"other\": true }");
    }

    @Test
    public void illegalBehavior() throws Exception {
        String json = "{ \"illegalBehavior\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getIllegalBehavior());
        assertValidJson(json);
        assertInvalidJson("{ \"illegalBehavior\": \"string\" }");
    }

    @Test
    public void addiction() throws Exception {
        String json = "{ \"addiction\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getAddiction());
        assertValidJson(json);
        assertInvalidJson("{ \"addiction\": \"string\" }");
    }

    @Test
    public void sexualDiseases() throws Exception {
        String json = "{ \"sexualDiseases\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getSexualDiseases());
        assertValidJson(json);
        assertInvalidJson("{ \"sexualDiseases\": \"string\" }");
    }

    @Test
    public void stigmatizeDiseases() throws Exception {
        String json = "{ \"stigmatizeDiseases\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getStigmatizeDiseases());
        assertValidJson(json);
        assertInvalidJson("{ \"stigmatizeDiseases\": \"string\" }");
    }

    @Test
    public void vulnerablePopulations() throws Exception {
        String json = "{ \"vulnerablePopulations\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getVulnerablePopulations());
        assertValidJson(json);
        assertInvalidJson("{ \"vulnerablePopulations\": \"string\" }");
    }

    @Test
    public void psychologicalTraits() throws Exception {
        String json = "{ \"psychologicalTraits\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getPsychologicalTraits());
        assertValidJson(json);
        assertInvalidJson("{ \"psychologicalTraits\": \"string\" }");
    }

    @Test
    public void nonBiomedical() throws Exception {
        String json = "{ \"nonBiomedical\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getNonBiomedical());
        assertValidJson(json);
        assertInvalidJson("{ \"nonBiomedical\": \"string\" }");
    }

    @Test
    public void ethicsApprovalRequired() throws Exception {
        String json = "{ \"ethicsApprovalRequired\": true }";
        DataUse dataUse = MAPPER.readValue(json, DataUse.class);
        assertTrue(dataUse.getEthicsApprovalRequired());
        assertValidJson(json);
        assertInvalidJson("{ \"ethicsApprovalRequired\": \"string\" }");
    }

}
