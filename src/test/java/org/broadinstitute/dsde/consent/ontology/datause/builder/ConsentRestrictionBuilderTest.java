package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class ConsentRestrictionBuilderTest {

    private UseRestrictionBuilder restrictionBuilder = new ConsentRestrictionBuilder();
    private UseRestriction everything = new Everything();

    @Test
    public void testGeneralUse() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, everything);
    }

    /**
     * All of the general use mixed cases are to ensure that GRU is ignored when
     * sub-conditions exist that would render it as a non-GRU use restriction
     */
    @Test
    public void testGeneralUseMixedCase1() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertNotEquals(restriction, everything);
    }

    @Test
    public void testGeneralUseMixedCase2() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.setPopulationRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertNotEquals(restriction, everything);
    }

    @Test
    public void testGeneralUseMixedCase3() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.setCommercialUse(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertNotEquals(restriction, everything);
    }

    @Test
    public void testGeneralUseMixedCase4() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.setGender("Male");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertNotEquals(restriction, everything);
    }

    @Test
    public void testGeneralUseMixedCase5() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        dataUse.setPediatric(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertNotEquals(restriction, everything);
    }

    @Test
    public void testMRdulUC1() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC1);
    }

    @Test
    public void testMRdulUC2() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setMethodsResearch(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC2);
    }

    @Test
    public void testMRdulUC3() {
        DataUse dataUse = new DataUse();
        dataUse.setMethodsResearch(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC3);
    }

    @Test
    public void testMRdulUC4() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setMethodsResearch(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.MRPdulUC4);
    }

    @Test
    public void testCSdulUC1() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC1);
    }

    @Test
    public void testCSdulUC2() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setControlSetOption("Yes");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC2);
    }

    @Test
    public void testCSdulUC3() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setControlSetOption("No");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertEquals(restriction, ConsentUseCases.CSdulUC3);
    }

}
