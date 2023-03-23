package org.broadinstitute.dsde.consent.ontology.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;



/**
 * Data Use Schema V3
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "generalUse",
    "hmbResearch",
    "diseaseRestrictions",
    "populationOriginsAncestry",
    "commercialUse",
    "nonProfitUse",
    "methodsResearch",
    "other",
    "secondaryOther",
    "ethicsApprovalRequired",
    "collaboratorRequired",
    "geographicalRestrictions",
    "geneticStudiesOnly",
    "publicationResults",
    "publicationMoratorium"
})
public class DataUseV3 {

    @JsonProperty("generalUse")
    private Boolean generalUse;
    @JsonProperty("hmbResearch")
    private Boolean hmbResearch;
    @JsonProperty("diseaseRestrictions")
    private List<String> diseaseRestrictions = new ArrayList<String>();
    @JsonProperty("populationOriginsAncestry")
    private Boolean populationOriginsAncestry;
    @JsonProperty("commercialUse")
    private Boolean commercialUse;
    @JsonProperty("nonProfitUse")
    private Boolean nonProfitUse;
    @JsonProperty("methodsResearch")
    private Boolean methodsResearch;
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

    @JsonProperty("generalUse")
    public Boolean getGeneralUse() {
        return generalUse;
    }

    @JsonProperty("generalUse")
    public void setGeneralUse(Boolean generalUse) {
        this.generalUse = generalUse;
    }

    @JsonProperty("hmbResearch")
    public Boolean getHmbResearch() {
        return hmbResearch;
    }

    @JsonProperty("hmbResearch")
    public void setHmbResearch(Boolean hmbResearch) {
        this.hmbResearch = hmbResearch;
    }

    @JsonProperty("diseaseRestrictions")
    public List<String> getDiseaseRestrictions() {
        return diseaseRestrictions;
    }

    @JsonProperty("diseaseRestrictions")
    public void setDiseaseRestrictions(List<String> diseaseRestrictions) {
        this.diseaseRestrictions = diseaseRestrictions;
    }

    @JsonProperty("populationOriginsAncestry")
    public Boolean getPopulationOriginsAncestry() {
        return populationOriginsAncestry;
    }

    @JsonProperty("populationOriginsAncestry")
    public void setPopulationOriginsAncestry(Boolean populationOriginsAncestry) {
        this.populationOriginsAncestry = populationOriginsAncestry;
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

    @JsonProperty("methodsResearch")
    public Boolean getMethodsResearch() {
        return methodsResearch;
    }

    @JsonProperty("methodsResearch")
    public void setMethodsResearch(Boolean methodsResearch) {
        this.methodsResearch = methodsResearch;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DataUseV3.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("generalUse");
        sb.append('=');
        sb.append(((this.generalUse == null)?"<null>":this.generalUse));
        sb.append(',');
        sb.append("hmbResearch");
        sb.append('=');
        sb.append(((this.hmbResearch == null)?"<null>":this.hmbResearch));
        sb.append(',');
        sb.append("diseaseRestrictions");
        sb.append('=');
        sb.append(((this.diseaseRestrictions == null)?"<null>":this.diseaseRestrictions));
        sb.append(',');
        sb.append("populationOriginsAncestry");
        sb.append('=');
        sb.append(((this.populationOriginsAncestry == null)?"<null>":this.populationOriginsAncestry));
        sb.append(',');
        sb.append("commercialUse");
        sb.append('=');
        sb.append(((this.commercialUse == null)?"<null>":this.commercialUse));
        sb.append(',');
        sb.append("nonProfitUse");
        sb.append('=');
        sb.append(((this.nonProfitUse == null)?"<null>":this.nonProfitUse));
        sb.append(',');
        sb.append("methodsResearch");
        sb.append('=');
        sb.append(((this.methodsResearch == null)?"<null>":this.methodsResearch));
        sb.append(',');
        sb.append("other");
        sb.append('=');
        sb.append(((this.other == null)?"<null>":this.other));
        sb.append(',');
        sb.append("secondaryOther");
        sb.append('=');
        sb.append(((this.secondaryOther == null)?"<null>":this.secondaryOther));
        sb.append(',');
        sb.append("ethicsApprovalRequired");
        sb.append('=');
        sb.append(((this.ethicsApprovalRequired == null)?"<null>":this.ethicsApprovalRequired));
        sb.append(',');
        sb.append("collaboratorRequired");
        sb.append('=');
        sb.append(((this.collaboratorRequired == null)?"<null>":this.collaboratorRequired));
        sb.append(',');
        sb.append("geographicalRestrictions");
        sb.append('=');
        sb.append(((this.geographicalRestrictions == null)?"<null>":this.geographicalRestrictions));
        sb.append(',');
        sb.append("geneticStudiesOnly");
        sb.append('=');
        sb.append(((this.geneticStudiesOnly == null)?"<null>":this.geneticStudiesOnly));
        sb.append(',');
        sb.append("publicationResults");
        sb.append('=');
        sb.append(((this.publicationResults == null)?"<null>":this.publicationResults));
        sb.append(',');
        sb.append("publicationMoratorium");
        sb.append('=');
        sb.append(((this.publicationMoratorium == null)?"<null>":this.publicationMoratorium));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.commercialUse == null)? 0 :this.commercialUse.hashCode()));
        result = ((result* 31)+((this.nonProfitUse == null)? 0 :this.nonProfitUse.hashCode()));
        result = ((result* 31)+((this.other == null)? 0 :this.other.hashCode()));
        result = ((result* 31)+((this.geneticStudiesOnly == null)? 0 :this.geneticStudiesOnly.hashCode()));
        result = ((result* 31)+((this.generalUse == null)? 0 :this.generalUse.hashCode()));
        result = ((result* 31)+((this.publicationResults == null)? 0 :this.publicationResults.hashCode()));
        result = ((result* 31)+((this.diseaseRestrictions == null)? 0 :this.diseaseRestrictions.hashCode()));
        result = ((result* 31)+((this.methodsResearch == null)? 0 :this.methodsResearch.hashCode()));
        result = ((result* 31)+((this.publicationMoratorium == null)? 0 :this.publicationMoratorium.hashCode()));
        result = ((result* 31)+((this.collaboratorRequired == null)? 0 :this.collaboratorRequired.hashCode()));
        result = ((result* 31)+((this.populationOriginsAncestry == null)? 0 :this.populationOriginsAncestry.hashCode()));
        result = ((result* 31)+((this.ethicsApprovalRequired == null)? 0 :this.ethicsApprovalRequired.hashCode()));
        result = ((result* 31)+((this.secondaryOther == null)? 0 :this.secondaryOther.hashCode()));
        result = ((result* 31)+((this.hmbResearch == null)? 0 :this.hmbResearch.hashCode()));
        result = ((result* 31)+((this.geographicalRestrictions == null)? 0 :this.geographicalRestrictions.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DataUseV3)) {
            return false;
        }
        DataUseV3 rhs = ((DataUseV3) other);
        return Objects.equals(this.commercialUse,
            rhs.commercialUse) && Objects.equals(this.nonProfitUse, rhs.nonProfitUse)
            && Objects.equals(
            this.other, rhs.other) && Objects.equals(this.generalUse, rhs.generalUse)
            && Objects.equals(this.diseaseRestrictions, rhs.diseaseRestrictions) && Objects.equals(
            this.methodsResearch, rhs.methodsResearch) && Objects.equals(
            this.populationOriginsAncestry, rhs.populationOriginsAncestry) && Objects.equals(
            this.secondaryOther, rhs.secondaryOther) && Objects.equals(this.hmbResearch,
            rhs.hmbResearch);
    }

}
