package org.broadinstitute.dsde.consent.ontology.resources.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Use Schema
 * <p>
 * Dynamically generated java class from jsonschema2pojo
 *
 * See: https://github.com/joelittlejohn/jsonschema2pojo
 * <code>jsonschema2pojo --source src/main/resources/data-use-schema.json --target java-gen</code>
 *
 * Needed to manually fix commons.lang -> commons.lang3 and some other minor simplifications
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "generalUse",
    "hmbResearch",
    "diseaseRestrictions",
    "populationOriginsAncestry",
    "commercialUseExcluded",
    "methodsResearchExcluded",
    "aggregateResearchResponse",
    "gender",
    "controlSetExcluded",
    "controlSetOption",
    "populationRestrictions",
    "pediatricLimited",
    "dateRestriction",
    "recontactingDataSubjects",
    "recontactMay",
    "recontactMust",
    "genomicPhenotypicData",
    "otherRestrictions",
    "cloudStorage",
    "geographicalRestrictions",
    "other",
    "illegalBehavior",
    "addiction",
    "sexualDiseases",
    "stigmatizeDiseases",
    "vulnerablePopulations",
    "psychologicalTraits",
    "nonBiomedical"
})
public class DataUseSchema {

    @JsonProperty("generalUse")
    private Boolean generalUse;
    @JsonProperty("hmbResearch")
    private Boolean hmbResearch;
    @JsonProperty("diseaseRestrictions")
    private List<String> diseaseRestrictions = new ArrayList<>();
    @JsonProperty("populationOriginsAncestry")
    private Boolean populationOriginsAncestry;
    @JsonProperty("commercialUseExcluded")
    private Boolean commercialUseExcluded;
    @JsonProperty("methodsResearchExcluded")
    private Boolean methodsResearchExcluded;
    @JsonProperty("aggregateResearchResponse")
    private String aggregateResearchResponse;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("controlSetExcluded")
    private Boolean controlSetExcluded;
    @JsonProperty("controlSetOption")
    private String controlSetOption;
    @JsonProperty("populationRestrictions")
    private List<String> populationRestrictions = new ArrayList<>();
    @JsonProperty("pediatricLimited")
    private Boolean pediatricLimited;
    @JsonProperty("dateRestriction")
    private String dateRestriction;
    @JsonProperty("recontactingDataSubjects")
    private Boolean recontactingDataSubjects;
    @JsonProperty("recontactMay")
    private String recontactMay;
    @JsonProperty("recontactMust")
    private String recontactMust;
    @JsonProperty("genomicPhenotypicData")
    private String genomicPhenotypicData;
    @JsonProperty("otherRestrictions")
    private Boolean otherRestrictions;
    @JsonProperty("cloudStorage")
    private String cloudStorage;
    @JsonProperty("geographicalRestrictions")
    private String geographicalRestrictions;
    @JsonProperty("other")
    private String other;
    @JsonProperty("illegalBehavior")
    private Boolean illegalBehavior;
    @JsonProperty("addiction")
    private Boolean addiction;
    @JsonProperty("sexualDiseases")
    private Boolean sexualDiseases;
    @JsonProperty("stigmatizeDiseases")
    private Boolean stigmatizeDiseases;
    @JsonProperty("vulnerablePopulations")
    private Boolean vulnerablePopulations;
    @JsonProperty("psychologicalTraits")
    private Boolean psychologicalTraits;
    @JsonProperty("nonBiomedical")
    private Boolean nonBiomedical;

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

    @JsonProperty("commercialUseExcluded")
    public Boolean getCommercialUseExcluded() {
        return commercialUseExcluded;
    }

    @JsonProperty("commercialUseExcluded")
    public void setCommercialUseExcluded(Boolean commercialUseExcluded) {
        this.commercialUseExcluded = commercialUseExcluded;
    }

    @JsonProperty("methodsResearchExcluded")
    public Boolean getMethodsResearchExcluded() {
        return methodsResearchExcluded;
    }

    @JsonProperty("methodsResearchExcluded")
    public void setMethodsResearchExcluded(Boolean methodsResearchExcluded) {
        this.methodsResearchExcluded = methodsResearchExcluded;
    }

    @JsonProperty("aggregateResearchResponse")
    public String getAggregateResearchResponse() {
        return aggregateResearchResponse;
    }

    @JsonProperty("aggregateResearchResponse")
    public void setAggregateResearchResponse(String aggregateResearchResponse) {
        this.aggregateResearchResponse = aggregateResearchResponse;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("controlSetExcluded")
    public Boolean getControlSetExcluded() {
        return controlSetExcluded;
    }

    @JsonProperty("controlSetExcluded")
    public void setControlSetExcluded(Boolean controlSetExcluded) {
        this.controlSetExcluded = controlSetExcluded;
    }

    @JsonProperty("controlSetOption")
    public String getControlSetOption() {
        return controlSetOption;
    }

    @JsonProperty("controlSetOption")
    public void setControlSetOption(String controlSetOption) {
        this.controlSetOption = controlSetOption;
    }

    @JsonProperty("populationRestrictions")
    public List<String> getPopulationRestrictions() {
        return populationRestrictions;
    }

    @JsonProperty("populationRestrictions")
    public void setPopulationRestrictions(List<String> populationRestrictions) {
        this.populationRestrictions = populationRestrictions;
    }

    @JsonProperty("pediatricLimited")
    public Boolean getPediatricLimited() {
        return pediatricLimited;
    }

    @JsonProperty("pediatricLimited")
    public void setPediatricLimited(Boolean pediatricLimited) {
        this.pediatricLimited = pediatricLimited;
    }

    @JsonProperty("dateRestriction")
    public String getDateRestriction() {
        return dateRestriction;
    }

    @JsonProperty("dateRestriction")
    public void setDateRestriction(String dateRestriction) {
        this.dateRestriction = dateRestriction;
    }

    @JsonProperty("recontactingDataSubjects")
    public Boolean getRecontactingDataSubjects() {
        return recontactingDataSubjects;
    }

    @JsonProperty("recontactingDataSubjects")
    public void setRecontactingDataSubjects(Boolean recontactingDataSubjects) {
        this.recontactingDataSubjects = recontactingDataSubjects;
    }

    @JsonProperty("recontactMay")
    public String getRecontactMay() {
        return recontactMay;
    }

    @JsonProperty("recontactMay")
    public void setRecontactMay(String recontactMay) {
        this.recontactMay = recontactMay;
    }

    @JsonProperty("recontactMust")
    public String getRecontactMust() {
        return recontactMust;
    }

    @JsonProperty("recontactMust")
    public void setRecontactMust(String recontactMust) {
        this.recontactMust = recontactMust;
    }

    @JsonProperty("genomicPhenotypicData")
    public String getGenomicPhenotypicData() {
        return genomicPhenotypicData;
    }

    @JsonProperty("genomicPhenotypicData")
    public void setGenomicPhenotypicData(String genomicPhenotypicData) {
        this.genomicPhenotypicData = genomicPhenotypicData;
    }

    @JsonProperty("otherRestrictions")
    public Boolean getOtherRestrictions() {
        return otherRestrictions;
    }

    @JsonProperty("otherRestrictions")
    public void setOtherRestrictions(Boolean otherRestrictions) {
        this.otherRestrictions = otherRestrictions;
    }

    @JsonProperty("cloudStorage")
    public String getCloudStorage() {
        return cloudStorage;
    }

    @JsonProperty("cloudStorage")
    public void setCloudStorage(String cloudStorage) {
        this.cloudStorage = cloudStorage;
    }

    @JsonProperty("geographicalRestrictions")
    public String getGeographicalRestrictions() {
        return geographicalRestrictions;
    }

    @JsonProperty("geographicalRestrictions")
    public void setGeographicalRestrictions(String geographicalRestrictions) {
        this.geographicalRestrictions = geographicalRestrictions;
    }

    @JsonProperty("other")
    public String getOther() {
        return other;
    }

    @JsonProperty("other")
    public void setOther(String other) {
        this.other = other;
    }

    @JsonProperty("illegalBehavior")
    public Boolean getIllegalBehavior() {
        return illegalBehavior;
    }

    @JsonProperty("illegalBehavior")
    public void setIllegalBehavior(Boolean illegalBehavior) {
        this.illegalBehavior = illegalBehavior;
    }

    @JsonProperty("addiction")
    public Boolean getAddiction() {
        return addiction;
    }

    @JsonProperty("addiction")
    public void setAddiction(Boolean addiction) {
        this.addiction = addiction;
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

    @JsonProperty("nonBiomedical")
    public Boolean getNonBiomedical() {
        return nonBiomedical;
    }

    @JsonProperty("nonBiomedical")
    public void setNonBiomedical(Boolean nonBiomedical) {
        this.nonBiomedical = nonBiomedical;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(generalUse).append(hmbResearch).append(diseaseRestrictions).append(populationOriginsAncestry).append(commercialUseExcluded).append(methodsResearchExcluded).append(aggregateResearchResponse).append(gender).append(controlSetExcluded).append(controlSetOption).append(populationRestrictions).append(pediatricLimited).append(dateRestriction).append(recontactingDataSubjects).append(recontactMay).append(recontactMust).append(genomicPhenotypicData).append(otherRestrictions).append(cloudStorage).append(geographicalRestrictions).append(other).append(illegalBehavior).append(addiction).append(sexualDiseases).append(stigmatizeDiseases).append(vulnerablePopulations).append(psychologicalTraits).append(nonBiomedical).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DataUseSchema)) {
            return false;
        }
        DataUseSchema rhs = ((DataUseSchema) other);
        return new EqualsBuilder().append(generalUse, rhs.generalUse).append(hmbResearch, rhs.hmbResearch).append(diseaseRestrictions, rhs.diseaseRestrictions).append(populationOriginsAncestry, rhs.populationOriginsAncestry).append(commercialUseExcluded, rhs.commercialUseExcluded).append(methodsResearchExcluded, rhs.methodsResearchExcluded).append(aggregateResearchResponse, rhs.aggregateResearchResponse).append(gender, rhs.gender).append(controlSetExcluded, rhs.controlSetExcluded).append(controlSetOption, rhs.controlSetOption).append(populationRestrictions, rhs.populationRestrictions).append(pediatricLimited, rhs.pediatricLimited).append(dateRestriction, rhs.dateRestriction).append(recontactingDataSubjects, rhs.recontactingDataSubjects).append(recontactMay, rhs.recontactMay).append(recontactMust, rhs.recontactMust).append(genomicPhenotypicData, rhs.genomicPhenotypicData).append(otherRestrictions, rhs.otherRestrictions).append(cloudStorage, rhs.cloudStorage).append(geographicalRestrictions, rhs.geographicalRestrictions).append(other, rhs.other).append(illegalBehavior, rhs.illegalBehavior).append(addiction, rhs.addiction).append(sexualDiseases, rhs.sexualDiseases).append(stigmatizeDiseases, rhs.stigmatizeDiseases).append(vulnerablePopulations, rhs.vulnerablePopulations).append(psychologicalTraits, rhs.psychologicalTraits).append(nonBiomedical, rhs.nonBiomedical).isEquals();
    }

}
