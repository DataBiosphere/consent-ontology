package org.broadinstitute.dsde.consent.ontology.datause.builder;

import org.broadinstitute.dsde.consent.ontology.datause.models.Everything;
import org.broadinstitute.dsde.consent.ontology.datause.models.UseRestriction;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class ConsentRestrictionBuilderTest {

    private UseRestrictionBuilder restrictionBuilder = new ConsentRestrictionBuilder();

    @Test
    public void testGeneralUse() {
        DataUse dataUse = new DataUse();
        dataUse.setGeneralUse(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(new Everything()));
    }

    @Test
    public void testMRdulUC1() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.MRPdulUC1));
    }

    @Test
    public void testMRdulUC2() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setMethodsResearch(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.MRPdulUC2));
    }

    @Test
    public void testMRdulUC3() {
        DataUse dataUse = new DataUse();
        dataUse.setMethodsResearch(false);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.MRPdulUC3));
    }

    @Test
    public void testMRdulUC4() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setMethodsResearch(true);
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.MRPdulUC4));
    }

    @Test
    public void testCSdulUC1() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.CSdulUC1));
    }

    @Test
    public void testCSdulUC2() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setControlSetOption("No");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.CSdulUC2));
    }

    @Test
    public void testCSdulUC3() {
        DataUse dataUse = new DataUse();
        dataUse.setDiseaseRestrictions(Collections.singletonList(ConsentUseCases.CANCER));
        dataUse.setControlSetOption("Yes");
        UseRestriction restriction = restrictionBuilder.buildUseRestriction(dataUse);
        Assert.assertTrue(restriction.equals(ConsentUseCases.CSdulUC3));
    }

}
