package org.broadinstitute.dsde.consent.ontology.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.text.StringEscapeUtils;

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
        return StringEscapeUtils.unescapeJson(other);
    }

    @JsonProperty("other")
    public void setOther(String other) {
        this.other = other;
    }

    @JsonProperty("secondaryOther")
    public String getSecondaryOther() {
        return StringEscapeUtils.unescapeJson(secondaryOther);
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
        return StringEscapeUtils.unescapeJson(geographicalRestrictions);
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
        return StringEscapeUtils.unescapeJson(publicationMoratorium);
    }

    @JsonProperty("publicationMoratorium")
    public void setPublicationMoratorium(String publicationMoratorium) {
        this.publicationMoratorium = publicationMoratorium;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DataUseV3.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append(StringEscapeUtils.unescapeJson("generalUse"));
        sb.append('=');
        sb.append((Objects.isNull(this.generalUse)?"<null>":this.generalUse));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("hmbResearch"));
        sb.append('=');
        sb.append((Objects.isNull(this.hmbResearch)?"<null>":this.hmbResearch));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("diseaseRestrictions"));
        sb.append('=');
        sb.append((Objects.isNull(this.diseaseRestrictions)?"<null>":this.diseaseRestrictions));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("populationOriginsAncestry"));
        sb.append('=');
        sb.append((Objects.isNull(this.populationOriginsAncestry)?"<null>":this.populationOriginsAncestry));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("commercialUse"));
        sb.append('=');
        sb.append((Objects.isNull(this.commercialUse)?"<null>":this.commercialUse));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("nonProfitUse"));
        sb.append('=');
        sb.append((Objects.isNull(this.nonProfitUse)?"<null>":this.nonProfitUse));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("methodsResearch"));
        sb.append('=');
        sb.append((Objects.isNull(this.methodsResearch)?"<null>":this.methodsResearch));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("other"));
        sb.append('=');
        sb.append((Objects.isNull(this.other)?"<null>":this.other));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("secondaryOther"));
        sb.append('=');
        sb.append((Objects.isNull(this.secondaryOther)?"<null>":this.secondaryOther));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("ethicsApprovalRequired"));
        sb.append('=');
        sb.append((Objects.isNull(this.ethicsApprovalRequired)?"<null>":this.ethicsApprovalRequired));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("collaboratorRequired"));
        sb.append('=');
        sb.append((Objects.isNull(this.collaboratorRequired)?"<null>":this.collaboratorRequired));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("geographicalRestrictions"));
        sb.append('=');
        sb.append((Objects.isNull(this.geographicalRestrictions)?"<null>":this.geographicalRestrictions));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("geneticStudiesOnly"));
        sb.append('=');
        sb.append((Objects.isNull(this.geneticStudiesOnly)?"<null>":this.geneticStudiesOnly));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("publicationResults"));
        sb.append('=');
        sb.append((Objects.isNull(this.publicationResults)?"<null>":this.publicationResults));
        sb.append(',');
        sb.append(StringEscapeUtils.unescapeJson("publicationMoratorium"));
        sb.append('=');
        sb.append((Objects.isNull(this.publicationMoratorium)?"<null>":this.publicationMoratorium));
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
        result = ((result* 31)+(Objects.isNull(this.commercialUse)? 0 :this.commercialUse.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.nonProfitUse)? 0 :this.nonProfitUse.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.other)? 0 :this.other.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.geneticStudiesOnly)? 0 :this.geneticStudiesOnly.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.generalUse)? 0 :this.generalUse.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.publicationResults)? 0 :this.publicationResults.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.diseaseRestrictions)? 0 :this.diseaseRestrictions.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.methodsResearch)? 0 :this.methodsResearch.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.publicationMoratorium)? 0 :this.publicationMoratorium.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.collaboratorRequired)? 0 :this.collaboratorRequired.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.populationOriginsAncestry)? 0 :this.populationOriginsAncestry.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.ethicsApprovalRequired)? 0 :this.ethicsApprovalRequired.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.secondaryOther)? 0 :this.secondaryOther.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.hmbResearch)? 0 :this.hmbResearch.hashCode()));
        result = ((result* 31)+(Objects.isNull(this.geographicalRestrictions)? 0 :this.geographicalRestrictions.hashCode()));
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
