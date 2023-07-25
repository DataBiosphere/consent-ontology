package org.broadinstitute.dsde.consent.ontology.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Data Use Schema V3
 * <p>
 * Dynamically generated java class from jsonschema2pojo
 * <p>
 * See: <a
 * href="https://github.com/joelittlejohn/jsonschema2pojo">https://github.com/joelittlejohn/jsonschema2pojo</a>
 * <code>jsonschema2pojo --source src/main/resources/data-use-v3.json --target java-gen</code>
 * <p>
 * Also see <a
 * href="https://jsonschemalint.com/#!/version/draft-07/markup/json">https://jsonschemalint.com/#!/version/draft-07/markup/json</a>
 * for validating json.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "generalUse",
    "diseaseRestrictions",
    "hmbResearch",
    "populationOriginsAncestry",
    "methodsResearch",
    "commercialUse",
    "nonProfitUse",
    "other",
    "secondaryOther",
    "ethicsApprovalRequired",
    "collaboratorRequired",
    "geographicalRestrictions",
    "geneticStudiesOnly",
    "publicationResults",
    "publicationMoratorium",
    "controls",
    "gender",
    "pediatric",
    "population",
    "illegalBehavior",
    "sexualDiseases",
    "stigmatizeDiseases",
    "vulnerablePopulations",
    "psychologicalTraits",
    "notHealth"
})
public class DataUseV3 {

  @JsonProperty("generalUse")
  private Boolean generalUse;
  @JsonProperty("diseaseRestrictions")
  private List<String> diseaseRestrictions = new ArrayList<>();
  @JsonProperty("hmbResearch")
  private Boolean hmbResearch;
  @JsonProperty("populationOriginsAncestry")
  private Boolean populationOriginsAncestry;
  @JsonProperty("methodsResearch")
  private Boolean methodsResearch;
  @JsonProperty("commercialUse")
  private Boolean commercialUse;
  @JsonProperty("nonProfitUse")
  private Boolean nonProfitUse;
  @JsonProperty("other")
  private String other;
  @JsonProperty("secondaryOther")
  private String secondaryOther;
  @JsonProperty("ethicsApprovalRequired")
  private Boolean ethicsApprovalRequired;
  @JsonProperty("collaboratorRequired")
  private Boolean collaboratorRequired;
  @JsonProperty("geographicalRestrictions")
  private String geographicalRestrictions;
  @JsonProperty("geneticStudiesOnly")
  private Boolean geneticStudiesOnly;
  @JsonProperty("publicationResults")
  private Boolean publicationResults;
  @JsonProperty("publicationMoratorium")
  private String publicationMoratorium;
  @JsonProperty("controls")
  private Boolean controls;
  @JsonProperty("gender")
  private String gender;
  @JsonProperty("pediatric")
  private Boolean pediatric;
  @JsonProperty("population")
  private Boolean population;
  @JsonProperty("illegalBehavior")
  private Boolean illegalBehavior;
  @JsonProperty("sexualDiseases")
  private Boolean sexualDiseases;
  @JsonProperty("stigmatizeDiseases")
  private Boolean stigmatizeDiseases;
  @JsonProperty("vulnerablePopulations")
  private Boolean vulnerablePopulations;
  @JsonProperty("psychologicalTraits")
  private Boolean psychologicalTraits;
  @JsonProperty("notHealth")
  private Boolean notHealth;

  @JsonProperty("generalUse")
  public Boolean getGeneralUse() {
    return generalUse;
  }

  @JsonProperty("generalUse")
  public void setGeneralUse(Boolean generalUse) {
    this.generalUse = generalUse;
  }

  @JsonProperty("diseaseRestrictions")
  public List<String> getDiseaseRestrictions() {
    return diseaseRestrictions;
  }

  @JsonProperty("diseaseRestrictions")
  public void setDiseaseRestrictions(List<String> diseaseRestrictions) {
    this.diseaseRestrictions = diseaseRestrictions;
  }

  @JsonProperty("hmbResearch")
  public Boolean getHmbResearch() {
    return hmbResearch;
  }

  @JsonProperty("hmbResearch")
  public void setHmbResearch(Boolean hmbResearch) {
    this.hmbResearch = hmbResearch;
  }

  @JsonProperty("populationOriginsAncestry")
  public Boolean getPopulationOriginsAncestry() {
    return populationOriginsAncestry;
  }

  @JsonProperty("populationOriginsAncestry")
  public void setPopulationOriginsAncestry(Boolean populationOriginsAncestry) {
    this.populationOriginsAncestry = populationOriginsAncestry;
  }

  @JsonProperty("methodsResearch")
  public Boolean getMethodsResearch() {
    return methodsResearch;
  }

  @JsonProperty("methodsResearch")
  public void setMethodsResearch(Boolean methodsResearch) {
    this.methodsResearch = methodsResearch;
  }

  @JsonProperty("commercialUse")
  public Boolean getCommercialUse() {
    return commercialUse;
  }

  @JsonProperty("commercialUse")
  public void setCommercialUse(Boolean commercialUse) {
    this.commercialUse = commercialUse;
  }

  @JsonProperty("nonProfitUse")
  public Boolean getNonProfitUse() {
    return nonProfitUse;
  }

  @JsonProperty("nonProfitUse")
  public void setNonProfitUse(Boolean nonProfitUse) {
    this.nonProfitUse = nonProfitUse;
  }

  @JsonProperty("other")
  public String getOther() {
    return other;
  }

  @JsonProperty("other")
  public void setOther(String other) {
    this.other = other;
  }

  @JsonProperty("secondaryOther")
  public String getSecondaryOther() {
    return secondaryOther;
  }

  @JsonProperty("secondaryOther")
  public void setSecondaryOther(String secondaryOther) {
    this.secondaryOther = secondaryOther;
  }

  @JsonProperty("ethicsApprovalRequired")
  public Boolean getEthicsApprovalRequired() {
    return ethicsApprovalRequired;
  }

  @JsonProperty("ethicsApprovalRequired")
  public void setEthicsApprovalRequired(Boolean ethicsApprovalRequired) {
    this.ethicsApprovalRequired = ethicsApprovalRequired;
  }

  @JsonProperty("collaboratorRequired")
  public Boolean getCollaboratorRequired() {
    return collaboratorRequired;
  }

  @JsonProperty("collaboratorRequired")
  public void setCollaboratorRequired(Boolean collaboratorRequired) {
    this.collaboratorRequired = collaboratorRequired;
  }

  @JsonProperty("geographicalRestrictions")
  public String getGeographicalRestrictions() {
    return geographicalRestrictions;
  }

  @JsonProperty("geographicalRestrictions")
  public void setGeographicalRestrictions(String geographicalRestrictions) {
    this.geographicalRestrictions = geographicalRestrictions;
  }

  @JsonProperty("geneticStudiesOnly")
  public Boolean getGeneticStudiesOnly() {
    return geneticStudiesOnly;
  }

  @JsonProperty("geneticStudiesOnly")
  public void setGeneticStudiesOnly(Boolean geneticStudiesOnly) {
    this.geneticStudiesOnly = geneticStudiesOnly;
  }

  @JsonProperty("publicationResults")
  public Boolean getPublicationResults() {
    return publicationResults;
  }

  @JsonProperty("publicationResults")
  public void setPublicationResults(Boolean publicationResults) {
    this.publicationResults = publicationResults;
  }

  @JsonProperty("publicationMoratorium")
  public String getPublicationMoratorium() {
    return publicationMoratorium;
  }

  @JsonProperty("publicationMoratorium")
  public void setPublicationMoratorium(String publicationMoratorium) {
    this.publicationMoratorium = publicationMoratorium;
  }

  @JsonProperty("controls")
  public Boolean getControls() {
    return controls;
  }

  @JsonProperty("controls")
  public void setControls(Boolean controls) {
    this.controls = controls;
  }

  @JsonProperty("gender")
  public String getGender() {
    return gender;
  }

  @JsonProperty("gender")
  public void setGender(String gender) {
    this.gender = gender;
  }

  @JsonProperty("pediatric")
  public Boolean getPediatric() {
    return pediatric;
  }

  @JsonProperty("pediatric")
  public void setPediatric(Boolean pediatric) {
    this.pediatric = pediatric;
  }

  @JsonProperty("population")
  public Boolean getPopulation() {
    return population;
  }

  @JsonProperty("population")
  public void setPopulation(Boolean population) {
    this.population = population;
  }

  @JsonProperty("illegalBehavior")
  public Boolean getIllegalBehavior() {
    return illegalBehavior;
  }

  @JsonProperty("illegalBehavior")
  public void setIllegalBehavior(Boolean illegalBehavior) {
    this.illegalBehavior = illegalBehavior;
  }

  @JsonProperty("sexualDiseases")
  public Boolean getSexualDiseases() {
    return sexualDiseases;
  }

  @JsonProperty("sexualDiseases")
  public void setSexualDiseases(Boolean sexualDiseases) {
    this.sexualDiseases = sexualDiseases;
  }

  @JsonProperty("stigmatizeDiseases")
  public Boolean getStigmatizeDiseases() {
    return stigmatizeDiseases;
  }

  @JsonProperty("stigmatizeDiseases")
  public void setStigmatizeDiseases(Boolean stigmatizeDiseases) {
    this.stigmatizeDiseases = stigmatizeDiseases;
  }

  @JsonProperty("vulnerablePopulations")
  public Boolean getVulnerablePopulations() {
    return vulnerablePopulations;
  }

  @JsonProperty("vulnerablePopulations")
  public void setVulnerablePopulations(Boolean vulnerablePopulations) {
    this.vulnerablePopulations = vulnerablePopulations;
  }

  @JsonProperty("psychologicalTraits")
  public Boolean getPsychologicalTraits() {
    return psychologicalTraits;
  }

  @JsonProperty("psychologicalTraits")
  public void setPsychologicalTraits(Boolean psychologicalTraits) {
    this.psychologicalTraits = psychologicalTraits;
  }

  @JsonProperty("notHealth")
  public Boolean getNotHealth() {
    return notHealth;
  }

  @JsonProperty("notHealth")
  public void setNotHealth(Boolean notHealth) {
    this.notHealth = notHealth;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(DataUseV3.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("generalUse");
    sb.append('=');
    sb.append(((this.generalUse == null) ? "<null>" : this.generalUse));
    sb.append(',');
    sb.append("diseaseRestrictions");
    sb.append('=');
    sb.append(((this.diseaseRestrictions == null) ? "<null>" : this.diseaseRestrictions));
    sb.append(',');
    sb.append("hmbResearch");
    sb.append('=');
    sb.append(((this.hmbResearch == null) ? "<null>" : this.hmbResearch));
    sb.append(',');
    sb.append("populationOriginsAncestry");
    sb.append('=');
    sb.append(
        ((this.populationOriginsAncestry == null) ? "<null>" : this.populationOriginsAncestry));
    sb.append(',');
    sb.append("methodsResearch");
    sb.append('=');
    sb.append(((this.methodsResearch == null) ? "<null>" : this.methodsResearch));
    sb.append(',');
    sb.append("commercialUse");
    sb.append('=');
    sb.append(((this.commercialUse == null) ? "<null>" : this.commercialUse));
    sb.append(',');
    sb.append("nonProfitUse");
    sb.append('=');
    sb.append(((this.nonProfitUse == null) ? "<null>" : this.nonProfitUse));
    sb.append(',');
    sb.append("other");
    sb.append('=');
    sb.append(((this.other == null) ? "<null>" : this.other));
    sb.append(',');
    sb.append("secondaryOther");
    sb.append('=');
    sb.append(((this.secondaryOther == null) ? "<null>" : this.secondaryOther));
    sb.append(',');
    sb.append("ethicsApprovalRequired");
    sb.append('=');
    sb.append(((this.ethicsApprovalRequired == null) ? "<null>" : this.ethicsApprovalRequired));
    sb.append(',');
    sb.append("collaboratorRequired");
    sb.append('=');
    sb.append(((this.collaboratorRequired == null) ? "<null>" : this.collaboratorRequired));
    sb.append(',');
    sb.append("geographicalRestrictions");
    sb.append('=');
    sb.append(((this.geographicalRestrictions == null) ? "<null>" : this.geographicalRestrictions));
    sb.append(',');
    sb.append("geneticStudiesOnly");
    sb.append('=');
    sb.append(((this.geneticStudiesOnly == null) ? "<null>" : this.geneticStudiesOnly));
    sb.append(',');
    sb.append("publicationResults");
    sb.append('=');
    sb.append(((this.publicationResults == null) ? "<null>" : this.publicationResults));
    sb.append(',');
    sb.append("publicationMoratorium");
    sb.append('=');
    sb.append(((this.publicationMoratorium == null) ? "<null>" : this.publicationMoratorium));
    sb.append(',');
    sb.append("controls");
    sb.append('=');
    sb.append(((this.controls == null) ? "<null>" : this.controls));
    sb.append(',');
    sb.append("gender");
    sb.append('=');
    sb.append(((this.gender == null) ? "<null>" : this.gender));
    sb.append(',');
    sb.append("pediatric");
    sb.append('=');
    sb.append(((this.pediatric == null) ? "<null>" : this.pediatric));
    sb.append(',');
    sb.append("population");
    sb.append('=');
    sb.append(((this.population == null) ? "<null>" : this.population));
    sb.append(',');
    sb.append("illegalBehavior");
    sb.append('=');
    sb.append(((this.illegalBehavior == null) ? "<null>" : this.illegalBehavior));
    sb.append(',');
    sb.append("sexualDiseases");
    sb.append('=');
    sb.append(((this.sexualDiseases == null) ? "<null>" : this.sexualDiseases));
    sb.append(',');
    sb.append("stigmatizeDiseases");
    sb.append('=');
    sb.append(((this.stigmatizeDiseases == null) ? "<null>" : this.stigmatizeDiseases));
    sb.append(',');
    sb.append("vulnerablePopulations");
    sb.append('=');
    sb.append(((this.vulnerablePopulations == null) ? "<null>" : this.vulnerablePopulations));
    sb.append(',');
    sb.append("psychologicalTraits");
    sb.append('=');
    sb.append(((this.psychologicalTraits == null) ? "<null>" : this.psychologicalTraits));
    sb.append(',');
    sb.append("notHealth");
    sb.append('=');
    sb.append(((this.notHealth == null) ? "<null>" : this.notHealth));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = ((result * 31) + ((this.commercialUse == null) ? 0 : this.commercialUse.hashCode()));
    result = ((result * 31) + ((this.other == null) ? 0 : this.other.hashCode()));
    result = ((result * 31) + ((this.nonProfitUse == null) ? 0 : this.nonProfitUse.hashCode()));
    result = ((result * 31) + ((this.controls == null) ? 0 : this.controls.hashCode()));
    result = ((result * 31) + ((this.psychologicalTraits == null) ? 0
        : this.psychologicalTraits.hashCode()));
    result = ((result * 31) + ((this.gender == null) ? 0 : this.gender.hashCode()));
    result = ((result * 31) + ((this.geneticStudiesOnly == null) ? 0
        : this.geneticStudiesOnly.hashCode()));
    result = ((result * 31) + ((this.generalUse == null) ? 0 : this.generalUse.hashCode()));
    result = ((result * 31) + ((this.publicationResults == null) ? 0
        : this.publicationResults.hashCode()));
    result = ((result * 31) + ((this.diseaseRestrictions == null) ? 0
        : this.diseaseRestrictions.hashCode()));
    result = ((result * 31) + ((this.pediatric == null) ? 0 : this.pediatric.hashCode()));
    result = ((result * 31) + ((this.vulnerablePopulations == null) ? 0
        : this.vulnerablePopulations.hashCode()));
    result = ((result * 31) + ((this.methodsResearch == null) ? 0
        : this.methodsResearch.hashCode()));
    result = ((result * 31) + ((this.publicationMoratorium == null) ? 0
        : this.publicationMoratorium.hashCode()));
    result = ((result * 31) + ((this.sexualDiseases == null) ? 0 : this.sexualDiseases.hashCode()));
    result = ((result * 31) + ((this.illegalBehavior == null) ? 0
        : this.illegalBehavior.hashCode()));
    result = ((result * 31) + ((this.collaboratorRequired == null) ? 0
        : this.collaboratorRequired.hashCode()));
    result = ((result * 31) + ((this.populationOriginsAncestry == null) ? 0
        : this.populationOriginsAncestry.hashCode()));
    result = ((result * 31) + ((this.ethicsApprovalRequired == null) ? 0
        : this.ethicsApprovalRequired.hashCode()));
    result = ((result * 31) + ((this.secondaryOther == null) ? 0 : this.secondaryOther.hashCode()));
    result = ((result * 31) + ((this.hmbResearch == null) ? 0 : this.hmbResearch.hashCode()));
    result = ((result * 31) + ((this.geographicalRestrictions == null) ? 0
        : this.geographicalRestrictions.hashCode()));
    result = ((result * 31) + ((this.population == null) ? 0 : this.population.hashCode()));
    result = ((result * 31) + ((this.notHealth == null) ? 0 : this.notHealth.hashCode()));
    result = ((result * 31) + ((this.stigmatizeDiseases == null) ? 0
        : this.stigmatizeDiseases.hashCode()));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof DataUseV3 rhs)) {
      return false;
    }
    return (Objects.equals(this.commercialUse, rhs.commercialUse)) &&
        Objects.equals(this.other, rhs.other) && (Objects.equals(this.nonProfitUse,
        rhs.nonProfitUse)) && (Objects.equals(this.controls, rhs.controls)) && (
        Objects.equals(this.psychologicalTraits, rhs.psychologicalTraits)) && Objects.equals(
        this.gender, rhs.gender) && (Objects.equals(this.geneticStudiesOnly,
        rhs.geneticStudiesOnly)) && (Objects.equals(this.generalUse, rhs.generalUse))
        && (Objects.equals(this.publicationResults,
        rhs.publicationResults)) && (Objects.equals(this.diseaseRestrictions,
        rhs.diseaseRestrictions)) && (Objects.equals(this.pediatric, rhs.pediatric)) && (
        Objects.equals(this.vulnerablePopulations, rhs.vulnerablePopulations)) && (Objects.equals(
        this.methodsResearch, rhs.methodsResearch))
        && Objects.equals(this.publicationMoratorium, rhs.publicationMoratorium) && (
        Objects.equals(this.sexualDiseases, rhs.sexualDiseases)) && (
        Objects.equals(this.illegalBehavior, rhs.illegalBehavior)) && (
        Objects.equals(this.collaboratorRequired, rhs.collaboratorRequired)) && (
        Objects.equals(this.populationOriginsAncestry, rhs.populationOriginsAncestry)) && (
        Objects.equals(this.ethicsApprovalRequired, rhs.ethicsApprovalRequired)) && Objects.equals(
        this.secondaryOther, rhs.secondaryOther) && (Objects.equals(this.hmbResearch,
        rhs.hmbResearch)) && Objects.equals(this.geographicalRestrictions,
        rhs.geographicalRestrictions) && (Objects.equals(
        this.population, rhs.population)) && (Objects.equals(this.notHealth, rhs.notHealth))
        && (Objects.equals(this.stigmatizeDiseases, rhs.stigmatizeDiseases));
  }

}

