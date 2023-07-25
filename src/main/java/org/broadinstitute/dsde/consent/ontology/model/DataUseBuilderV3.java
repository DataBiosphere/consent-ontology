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

  public DataUseBuilderV3 setGeographicalRestrictions(String geographicalRestrictions) {
    du.setGeographicalRestrictions(geographicalRestrictions);
    return this;
  }

  public DataUseBuilderV3 setGeneticStudiesOnly(boolean geneticStudiesOnly) {
    du.setGeneticStudiesOnly(geneticStudiesOnly);
    return this;
  }

  public DataUseBuilderV3 setPublicationResults(boolean publicationResults) {
    du.setPublicationResults(publicationResults);
    return this;
  }

  public DataUseBuilderV3 setPublicationMoratorium(String publicationMoratorium) {
    du.setPublicationMoratorium(publicationMoratorium);
    return this;
  }

  public DataUseBuilderV3 setControls(boolean controls) {
    du.setControls(controls);
    return this;
  }

  public DataUseBuilderV3 setGender(String gender) {
    du.setGender(gender);
    return this;
  }

  public DataUseBuilderV3 setPediatric(boolean pediatric) {
    du.setPediatric(pediatric);
    return this;
  }

  public DataUseBuilderV3 setPopulation(boolean population) {
    du.setPopulation(population);
    return this;
  }

  public DataUseBuilderV3 setIllegalBehavior(boolean illegalBehavior) {
    du.setIllegalBehavior(illegalBehavior);
    return this;
  }

  public DataUseBuilderV3 setSexualDiseases(boolean sexualDiseases) {
    du.setSexualDiseases(sexualDiseases);
    return this;
  }

  public DataUseBuilderV3 setStigmatizeDiseases(boolean stigmatizeDiseases) {
    du.setStigmatizeDiseases(stigmatizeDiseases);
    return this;
  }

  public DataUseBuilderV3 setVulnerablePopulations(boolean vulnerablePopulations) {
    du.setVulnerablePopulations(vulnerablePopulations);
    return this;
  }

  public DataUseBuilderV3 setPsychologicalTraits(boolean psychologicalTraits) {
    du.setPsychologicalTraits(psychologicalTraits);
    return this;
  }

  public DataUseBuilderV3 setNotHealth(boolean notHealth) {
    du.setNotHealth(notHealth);
    return this;
  }

}
