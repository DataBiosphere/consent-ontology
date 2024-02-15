package org.broadinstitute.dsde.consent.ontology.model;

import java.util.List;

/**
 * Syntactic sugar for creating a DataUseV4 object.
 */
public class DataUseBuilderV4 {

  private DataUseV4 du;

  public DataUseBuilderV4() {
    du = new DataUseV4();
  }

  public DataUseV4 build() {
    return du;
  }


  public DataUseBuilderV4 setGeneralUse(Boolean generalUse) {
    du.setGeneralUse(generalUse);
    return this;
  }

  public DataUseBuilderV4 setHmbResearch(Boolean hmbResearch) {
    du.setHmbResearch(hmbResearch);
    return this;
  }

  public DataUseBuilderV4 setDiseaseRestrictions(List<String> diseaseRestrictions) {
    du.setDiseaseRestrictions(diseaseRestrictions);
    return this;
  }

  public DataUseBuilderV4 setPopulationOriginsAncestry(Boolean populationOriginsAncestry) {
    du.setPopulationOriginsAncestry(populationOriginsAncestry);
    return this;
  }

  public DataUseBuilderV4 setNonProfitUse(Boolean nonProfitUse) {
    du.setNonProfitUse(nonProfitUse);
    return this;
  }

  public DataUseBuilderV4 setMethodsResearch(Boolean methodsResearch) {
    du.setMethodsResearch(methodsResearch);
    return this;
  }

  public DataUseBuilderV4 setOther(String other) {
    du.setOther(other);
    return this;
  }

  public DataUseBuilderV4 setSecondaryOther(String secondaryOther) {
    du.setSecondaryOther(secondaryOther);
    return this;
  }

  public DataUseBuilderV4 setEthicsApprovalRequired(Boolean ethicsApprovalRequired) {
    du.setEthicsApprovalRequired(ethicsApprovalRequired);
    return this;
  }

  public DataUseBuilderV4 setCollaboratorRequired(boolean collaboratorRequired) {
    du.setCollaboratorRequired(collaboratorRequired);
    return this;
  }

  public DataUseBuilderV4 setGeographicalRestrictions(String geographicalRestrictions) {
    du.setGeographicalRestrictions(geographicalRestrictions);
    return this;
  }

  public DataUseBuilderV4 setGeneticStudiesOnly(boolean geneticStudiesOnly) {
    du.setGeneticStudiesOnly(geneticStudiesOnly);
    return this;
  }

  public DataUseBuilderV4 setPublicationResults(boolean publicationResults) {
    du.setPublicationResults(publicationResults);
    return this;
  }

  public DataUseBuilderV4 setPublicationMoratorium(String publicationMoratorium) {
    du.setPublicationMoratorium(publicationMoratorium);
    return this;
  }

  public DataUseBuilderV4 setControls(boolean controls) {
    du.setControls(controls);
    return this;
  }

  public DataUseBuilderV4 setGender(String gender) {
    du.setGender(gender);
    return this;
  }

  public DataUseBuilderV4 setPediatric(boolean pediatric) {
    du.setPediatric(pediatric);
    return this;
  }

  public DataUseBuilderV4 setPopulation(boolean population) {
    du.setPopulation(population);
    return this;
  }

  public DataUseBuilderV4 setIllegalBehavior(boolean illegalBehavior) {
    du.setIllegalBehavior(illegalBehavior);
    return this;
  }

  public DataUseBuilderV4 setSexualDiseases(boolean sexualDiseases) {
    du.setSexualDiseases(sexualDiseases);
    return this;
  }

  public DataUseBuilderV4 setStigmatizeDiseases(boolean stigmatizeDiseases) {
    du.setStigmatizeDiseases(stigmatizeDiseases);
    return this;
  }

  public DataUseBuilderV4 setVulnerablePopulations(boolean vulnerablePopulations) {
    du.setVulnerablePopulations(vulnerablePopulations);
    return this;
  }

  public DataUseBuilderV4 setPsychologicalTraits(boolean psychologicalTraits) {
    du.setPsychologicalTraits(psychologicalTraits);
    return this;
  }

  public DataUseBuilderV4 setNotHealth(boolean notHealth) {
    du.setNotHealth(notHealth);
    return this;
  }

}
