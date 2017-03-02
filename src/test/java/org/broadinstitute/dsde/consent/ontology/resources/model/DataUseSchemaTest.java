package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DataUseSchemaTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void generalUse() throws Exception {
        String json = "{ \"generalUse\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getGeneralUse());
    }

    @Test
    public void hmbResearch() throws Exception {
        String json = "{ \"hmbResearch\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getHmbResearch());
    }

    @Test
    public void diseaseRestrictions() throws Exception {
        String json = "{ \"diseaseRestrictions\": [\"one\", \"two\"] }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getDiseaseRestrictions().size() == 2);
    }

    @Test
    public void populationOriginsAncestry() throws Exception {
        String json = "{ \"populationOriginsAncestry\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getPopulationOriginsAncestry());
    }

    @Test
    public void commercialUseExcluded() throws Exception {
        String json = "{ \"commercialUseExcluded\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getCommercialUseExcluded());
    }

    @Test
    public void methodsResearchExcluded() throws Exception {
        String json = "{ \"methodsResearchExcluded\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getMethodsResearchExcluded());
    }

    @Test
    public void aggregateResearchResponse() throws Exception {
        String json = "{ \"aggregateResearchResponse\": \"Yes\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getAggregateResearchResponse().equals("Yes"));
    }

    @Test
    public void gender() throws Exception {
        String json = "{ \"gender\": \"Male\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getGender().equals("Male"));
    }

    @Test
    public void controlSetExcluded() throws Exception {
        String json = "{ \"controlSetExcluded\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getControlSetExcluded());
    }

    @Test
    public void controlSetOption() throws Exception {
        String json = "{ \"controlSetOption\": \"Yes\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getControlSetOption().equals("Yes"));
    }

    @Test
    public void populationRestrictions() throws Exception {
        String json = "{ \"populationRestrictions\": [\"one\", \"two\"] }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getPopulationRestrictions().size() == 2);
    }

    @Test
    public void pediatricLimited() throws Exception {
        String json = "{ \"pediatricLimited\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getPediatricLimited());
    }

    @Test
    public void dateRestriction() throws Exception {
        String json = "{ \"dateRestriction\": \"date\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getDateRestriction().equals("date"));
    }

    @Test
    public void recontactingDataSubjects() throws Exception {
        String json = "{ \"recontactingDataSubjects\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getRecontactingDataSubjects());
    }

    @Test
    public void recontactMay() throws Exception {
        String json = "{ \"recontactMay\": \"may\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getRecontactMay().equals("may"));
    }

    @Test
    public void recontactMust() throws Exception {
        String json = "{ \"recontactMust\": \"must\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getRecontactMust().equals("must"));
    }

    @Test
    public void genomicPhenotypicData() throws Exception {
        String json = "{ \"genomicPhenotypicData\": \"data\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getGenomicPhenotypicData().equals("data"));
    }

    @Test
    public void otherRestrictions() throws Exception {
        String json = "{ \"otherRestrictions\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getOtherRestrictions());
    }

    @Test
    public void cloudStorage() throws Exception {
        String json = "{ \"cloudStorage\": \"cloud\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getCloudStorage().equals("cloud"));
    }

    @Test
    public void geographicalRestrictions() throws Exception {
        String json = "{ \"geographicalRestrictions\": \"geo\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getGeographicalRestrictions().equals("geo"));
    }

    @Test
    public void other() throws Exception {
        String json = "{ \"other\": \"other\" }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getOther().equals("other"));
    }

    @Test
    public void illegalBehavior() throws Exception {
        String json = "{ \"illegalBehavior\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getIllegalBehavior());
    }

    @Test
    public void addiction() throws Exception {
        String json = "{ \"addiction\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getAddiction());
    }

    @Test
    public void sexualDiseases() throws Exception {
        String json = "{ \"sexualDiseases\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getSexualDiseases());
    }

    @Test
    public void stigmatizeDiseases() throws Exception {
        String json = "{ \"stigmatizeDiseases\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getStigmatizeDiseases());
    }

    @Test
    public void vulnerablePopulations() throws Exception {
        String json = "{ \"vulnerablePopulations\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getVulnerablePopulations());
    }

    @Test
    public void psychologicalTraits() throws Exception {
        String json = "{ \"psychologicalTraits\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getPsychologicalTraits());
    }

    @Test
    public void nonBiomedical() throws Exception {
        String json = "{ \"nonBiomedical\": true }";
        DataUseSchema schema = mapper.readValue(json, DataUseSchema.class);
        assertTrue(schema.getNonBiomedical());
    }

}
