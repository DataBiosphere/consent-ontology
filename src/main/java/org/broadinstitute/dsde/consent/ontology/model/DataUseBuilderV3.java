package org.broadinstitute.dsde.consent.ontology.model;

import java.util.List;

/**
 * Syntactic sugar for creating a DataUseV3 object.
 */
public class DataUseBuilderV3 {
  private DataUseV3 du;

  public DataUseBuilderV3() {
    du = new DataUseV3();
  }

  public DataUseV3 build() {
    return du;
  }


  public DataUseBuilderV3 setGeneralUse(Boolean generalUse) {
    du.setGeneralUse(generalUse);
    return this;
  }

  public DataUseBuilderV3 setHmbResearch(Boolean hmbResearch) {
    du.setHmbResearch(hmbResearch);
    return this;
  }

  public DataUseBuilderV3 setDiseaseRestrictions(List<String> diseaseRestrictions) {
    du.setDiseaseRestrictions(diseaseRestrictions);
    return this;
  }

  public DataUseBuilderV3 setPopulationOriginsAncestry(Boolean populationOriginsAncestry) {
    du.setPopulationOriginsAncestry(populationOriginsAncestry);
    return this;
  }

  public DataUseBuilderV3 setCommercialUse(Boolean commercialUse) {
    du.setCommercialUse(commercialUse);
    return this;
  }

  public DataUseBuilderV3 setNonProfitUse(Boolean nonProfitUse) {
    du.setNonProfitUse(nonProfitUse);
    return this;
  }

  public DataUseBuilderV3 setMethodsResearch(Boolean methodsResearch) {
    du.setMethodsResearch(methodsResearch);
    return this;
  }

  public DataUseBuilderV3 setOther(String other) {
    du.setOther(other);
    return this;
  }

  public DataUseBuilderV3 setSecondaryOther(String secondaryOther) {
    du.setSecondaryOther(secondaryOther);
    return this;
  }

  public DataUseBuilderV3 setEthicsApprovalRequired(Boolean ethicsApprovalRequired) {
    du.setEthicsApprovalRequired(ethicsApprovalRequired);
    return this;
  }

  public DataUseBuilderV3 setCollaboratorRequired(boolean collaboratorRequired) {
    du.setCollaboratorRequired(collaboratorRequired);
    return this;
  }
}
