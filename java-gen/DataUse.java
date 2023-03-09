import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Data Use Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "generalUse",
    "hmbResearch",
    "diseaseRestrictions",
    "populationOriginsAncestry",
    "populationStructure",
    "commercialUse",
    "methodsResearch",
    "aggregateResearch",
    "controlSetOption",
    "gender",
    "pediatric",
    "populationRestrictions",
    "dateRestriction",
    "recontactingDataSubjects",
    "recontactMay",
    "recontactMust",
    "genomicPhenotypicData",
    "otherRestrictions",
    "other",
    "secondaryOther",
    "cloudStorage",
    "ethicsApprovalRequired",
    "collaboratorRequired",
    "geographicalRestrictions",
    "illegalBehavior",
    "addiction",
    "sexualDiseases",
    "stigmatizeDiseases",
    "vulnerablePopulations",
    "psychologicalTraits",
    "nonBiomedical",
    "manualReview",
    "geneticStudiesOnly",
    "publicationResults",
    "genomicResults",
    "genomicSummaryResults",
    "collaborationInvestigators",
    "publicationMoratorium"
})
public class DataUse {

    @JsonProperty("generalUse")
    private Boolean generalUse;
    @JsonProperty("hmbResearch")
    private Boolean hmbResearch;
    @JsonProperty("diseaseRestrictions")
    private List<String> diseaseRestrictions = new ArrayList<String>();
    @JsonProperty("populationOriginsAncestry")
    private Boolean populationOriginsAncestry;
    @JsonProperty("populationStructure")
    private Boolean populationStructure;
    @JsonProperty("commercialUse")
    private Boolean commercialUse;
    @JsonProperty("methodsResearch")
    private Boolean methodsResearch;
    @JsonProperty("aggregateResearch")
    private DataUse.AggregateResearch aggregateResearch;
    @JsonProperty("controlSetOption")
    private DataUse.ControlSetOption controlSetOption;
    @JsonProperty("gender")
    private DataUse.Gender gender;
    @JsonProperty("pediatric")
    private Boolean pediatric;
    @JsonProperty("populationRestrictions")
    private List<String> populationRestrictions = new ArrayList<String>();
    @JsonProperty("dateRestriction")
    private String dateRestriction;
    @JsonProperty("recontactingDataSubjects")
    private Boolean recontactingDataSubjects;
    @JsonProperty("recontactMay")
    private String recontactMay;
    @JsonProperty("recontactMust")
    private String recontactMust;
    @JsonProperty("genomicPhenotypicData")
    private DataUse.GenomicPhenotypicData genomicPhenotypicData;
    @JsonProperty("otherRestrictions")
    private Boolean otherRestrictions;
    @JsonProperty("other")
    private String other;
    @JsonProperty("secondaryOther")
    private String secondaryOther;
    @JsonProperty("cloudStorage")
    private DataUse.CloudStorage cloudStorage;
    @JsonProperty("ethicsApprovalRequired")
    private Boolean ethicsApprovalRequired;
    @JsonProperty("collaboratorRequired")
    private Boolean collaboratorRequired;
    @JsonProperty("geographicalRestrictions")
    private String geographicalRestrictions;
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
    @JsonProperty("manualReview")
    private Boolean manualReview;
    @JsonProperty("geneticStudiesOnly")
    private Boolean geneticStudiesOnly;
    @JsonProperty("publicationResults")
    private Boolean publicationResults;
    @JsonProperty("genomicResults")
    private Boolean genomicResults;
    @JsonProperty("genomicSummaryResults")
    private String genomicSummaryResults;
    @JsonProperty("collaborationInvestigators")
    private Boolean collaborationInvestigators;
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

    @JsonProperty("populationStructure")
    public Boolean getPopulationStructure() {
        return populationStructure;
    }

    @JsonProperty("populationStructure")
    public void setPopulationStructure(Boolean populationStructure) {
        this.populationStructure = populationStructure;
    }

    @JsonProperty("commercialUse")
    public Boolean getCommercialUse() {
        return commercialUse;
    }

    @JsonProperty("commercialUse")
    public void setCommercialUse(Boolean commercialUse) {
        this.commercialUse = commercialUse;
    }

    @JsonProperty("methodsResearch")
    public Boolean getMethodsResearch() {
        return methodsResearch;
    }

    @JsonProperty("methodsResearch")
    public void setMethodsResearch(Boolean methodsResearch) {
        this.methodsResearch = methodsResearch;
    }

    @JsonProperty("aggregateResearch")
    public DataUse.AggregateResearch getAggregateResearch() {
        return aggregateResearch;
    }

    @JsonProperty("aggregateResearch")
    public void setAggregateResearch(DataUse.AggregateResearch aggregateResearch) {
        this.aggregateResearch = aggregateResearch;
    }

    @JsonProperty("controlSetOption")
    public DataUse.ControlSetOption getControlSetOption() {
        return controlSetOption;
    }

    @JsonProperty("controlSetOption")
    public void setControlSetOption(DataUse.ControlSetOption controlSetOption) {
        this.controlSetOption = controlSetOption;
    }

    @JsonProperty("gender")
    public DataUse.Gender getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(DataUse.Gender gender) {
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

    @JsonProperty("populationRestrictions")
    public List<String> getPopulationRestrictions() {
        return populationRestrictions;
    }

    @JsonProperty("populationRestrictions")
    public void setPopulationRestrictions(List<String> populationRestrictions) {
        this.populationRestrictions = populationRestrictions;
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
    public DataUse.GenomicPhenotypicData getGenomicPhenotypicData() {
        return genomicPhenotypicData;
    }

    @JsonProperty("genomicPhenotypicData")
    public void setGenomicPhenotypicData(DataUse.GenomicPhenotypicData genomicPhenotypicData) {
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

    @JsonProperty("cloudStorage")
    public DataUse.CloudStorage getCloudStorage() {
        return cloudStorage;
    }

    @JsonProperty("cloudStorage")
    public void setCloudStorage(DataUse.CloudStorage cloudStorage) {
        this.cloudStorage = cloudStorage;
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

    @JsonProperty("manualReview")
    public Boolean getManualReview() {
        return manualReview;
    }

    @JsonProperty("manualReview")
    public void setManualReview(Boolean manualReview) {
        this.manualReview = manualReview;
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

    @JsonProperty("genomicResults")
    public Boolean getGenomicResults() {
        return genomicResults;
    }

    @JsonProperty("genomicResults")
    public void setGenomicResults(Boolean genomicResults) {
        this.genomicResults = genomicResults;
    }

    @JsonProperty("genomicSummaryResults")
    public String getGenomicSummaryResults() {
        return genomicSummaryResults;
    }

    @JsonProperty("genomicSummaryResults")
    public void setGenomicSummaryResults(String genomicSummaryResults) {
        this.genomicSummaryResults = genomicSummaryResults;
    }

    @JsonProperty("collaborationInvestigators")
    public Boolean getCollaborationInvestigators() {
        return collaborationInvestigators;
    }

    @JsonProperty("collaborationInvestigators")
    public void setCollaborationInvestigators(Boolean collaborationInvestigators) {
        this.collaborationInvestigators = collaborationInvestigators;
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
        sb.append(DataUse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("populationStructure");
        sb.append('=');
        sb.append(((this.populationStructure == null)?"<null>":this.populationStructure));
        sb.append(',');
        sb.append("commercialUse");
        sb.append('=');
        sb.append(((this.commercialUse == null)?"<null>":this.commercialUse));
        sb.append(',');
        sb.append("methodsResearch");
        sb.append('=');
        sb.append(((this.methodsResearch == null)?"<null>":this.methodsResearch));
        sb.append(',');
        sb.append("aggregateResearch");
        sb.append('=');
        sb.append(((this.aggregateResearch == null)?"<null>":this.aggregateResearch));
        sb.append(',');
        sb.append("controlSetOption");
        sb.append('=');
        sb.append(((this.controlSetOption == null)?"<null>":this.controlSetOption));
        sb.append(',');
        sb.append("gender");
        sb.append('=');
        sb.append(((this.gender == null)?"<null>":this.gender));
        sb.append(',');
        sb.append("pediatric");
        sb.append('=');
        sb.append(((this.pediatric == null)?"<null>":this.pediatric));
        sb.append(',');
        sb.append("populationRestrictions");
        sb.append('=');
        sb.append(((this.populationRestrictions == null)?"<null>":this.populationRestrictions));
        sb.append(',');
        sb.append("dateRestriction");
        sb.append('=');
        sb.append(((this.dateRestriction == null)?"<null>":this.dateRestriction));
        sb.append(',');
        sb.append("recontactingDataSubjects");
        sb.append('=');
        sb.append(((this.recontactingDataSubjects == null)?"<null>":this.recontactingDataSubjects));
        sb.append(',');
        sb.append("recontactMay");
        sb.append('=');
        sb.append(((this.recontactMay == null)?"<null>":this.recontactMay));
        sb.append(',');
        sb.append("recontactMust");
        sb.append('=');
        sb.append(((this.recontactMust == null)?"<null>":this.recontactMust));
        sb.append(',');
        sb.append("genomicPhenotypicData");
        sb.append('=');
        sb.append(((this.genomicPhenotypicData == null)?"<null>":this.genomicPhenotypicData));
        sb.append(',');
        sb.append("otherRestrictions");
        sb.append('=');
        sb.append(((this.otherRestrictions == null)?"<null>":this.otherRestrictions));
        sb.append(',');
        sb.append("other");
        sb.append('=');
        sb.append(((this.other == null)?"<null>":this.other));
        sb.append(',');
        sb.append("secondaryOther");
        sb.append('=');
        sb.append(((this.secondaryOther == null)?"<null>":this.secondaryOther));
        sb.append(',');
        sb.append("cloudStorage");
        sb.append('=');
        sb.append(((this.cloudStorage == null)?"<null>":this.cloudStorage));
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
        sb.append("illegalBehavior");
        sb.append('=');
        sb.append(((this.illegalBehavior == null)?"<null>":this.illegalBehavior));
        sb.append(',');
        sb.append("addiction");
        sb.append('=');
        sb.append(((this.addiction == null)?"<null>":this.addiction));
        sb.append(',');
        sb.append("sexualDiseases");
        sb.append('=');
        sb.append(((this.sexualDiseases == null)?"<null>":this.sexualDiseases));
        sb.append(',');
        sb.append("stigmatizeDiseases");
        sb.append('=');
        sb.append(((this.stigmatizeDiseases == null)?"<null>":this.stigmatizeDiseases));
        sb.append(',');
        sb.append("vulnerablePopulations");
        sb.append('=');
        sb.append(((this.vulnerablePopulations == null)?"<null>":this.vulnerablePopulations));
        sb.append(',');
        sb.append("psychologicalTraits");
        sb.append('=');
        sb.append(((this.psychologicalTraits == null)?"<null>":this.psychologicalTraits));
        sb.append(',');
        sb.append("nonBiomedical");
        sb.append('=');
        sb.append(((this.nonBiomedical == null)?"<null>":this.nonBiomedical));
        sb.append(',');
        sb.append("manualReview");
        sb.append('=');
        sb.append(((this.manualReview == null)?"<null>":this.manualReview));
        sb.append(',');
        sb.append("geneticStudiesOnly");
        sb.append('=');
        sb.append(((this.geneticStudiesOnly == null)?"<null>":this.geneticStudiesOnly));
        sb.append(',');
        sb.append("publicationResults");
        sb.append('=');
        sb.append(((this.publicationResults == null)?"<null>":this.publicationResults));
        sb.append(',');
        sb.append("genomicResults");
        sb.append('=');
        sb.append(((this.genomicResults == null)?"<null>":this.genomicResults));
        sb.append(',');
        sb.append("genomicSummaryResults");
        sb.append('=');
        sb.append(((this.genomicSummaryResults == null)?"<null>":this.genomicSummaryResults));
        sb.append(',');
        sb.append("collaborationInvestigators");
        sb.append('=');
        sb.append(((this.collaborationInvestigators == null)?"<null>":this.collaborationInvestigators));
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
        result = ((result* 31)+((this.otherRestrictions == null)? 0 :this.otherRestrictions.hashCode()));
        result = ((result* 31)+((this.populationStructure == null)? 0 :this.populationStructure.hashCode()));
        result = ((result* 31)+((this.other == null)? 0 :this.other.hashCode()));
        result = ((result* 31)+((this.recontactingDataSubjects == null)? 0 :this.recontactingDataSubjects.hashCode()));
        result = ((result* 31)+((this.psychologicalTraits == null)? 0 :this.psychologicalTraits.hashCode()));
        result = ((result* 31)+((this.gender == null)? 0 :this.gender.hashCode()));
        result = ((result* 31)+((this.populationRestrictions == null)? 0 :this.populationRestrictions.hashCode()));
        result = ((result* 31)+((this.geneticStudiesOnly == null)? 0 :this.geneticStudiesOnly.hashCode()));
        result = ((result* 31)+((this.generalUse == null)? 0 :this.generalUse.hashCode()));
        result = ((result* 31)+((this.publicationResults == null)? 0 :this.publicationResults.hashCode()));
        result = ((result* 31)+((this.diseaseRestrictions == null)? 0 :this.diseaseRestrictions.hashCode()));
        result = ((result* 31)+((this.pediatric == null)? 0 :this.pediatric.hashCode()));
        result = ((result* 31)+((this.vulnerablePopulations == null)? 0 :this.vulnerablePopulations.hashCode()));
        result = ((result* 31)+((this.collaborationInvestigators == null)? 0 :this.collaborationInvestigators.hashCode()));
        result = ((result* 31)+((this.genomicSummaryResults == null)? 0 :this.genomicSummaryResults.hashCode()));
        result = ((result* 31)+((this.methodsResearch == null)? 0 :this.methodsResearch.hashCode()));
        result = ((result* 31)+((this.publicationMoratorium == null)? 0 :this.publicationMoratorium.hashCode()));
        result = ((result* 31)+((this.aggregateResearch == null)? 0 :this.aggregateResearch.hashCode()));
        result = ((result* 31)+((this.sexualDiseases == null)? 0 :this.sexualDiseases.hashCode()));
        result = ((result* 31)+((this.controlSetOption == null)? 0 :this.controlSetOption.hashCode()));
        result = ((result* 31)+((this.illegalBehavior == null)? 0 :this.illegalBehavior.hashCode()));
        result = ((result* 31)+((this.collaboratorRequired == null)? 0 :this.collaboratorRequired.hashCode()));
        result = ((result* 31)+((this.populationOriginsAncestry == null)? 0 :this.populationOriginsAncestry.hashCode()));
        result = ((result* 31)+((this.ethicsApprovalRequired == null)? 0 :this.ethicsApprovalRequired.hashCode()));
        result = ((result* 31)+((this.secondaryOther == null)? 0 :this.secondaryOther.hashCode()));
        result = ((result* 31)+((this.addiction == null)? 0 :this.addiction.hashCode()));
        result = ((result* 31)+((this.hmbResearch == null)? 0 :this.hmbResearch.hashCode()));
        result = ((result* 31)+((this.recontactMust == null)? 0 :this.recontactMust.hashCode()));
        result = ((result* 31)+((this.genomicPhenotypicData == null)? 0 :this.genomicPhenotypicData.hashCode()));
        result = ((result* 31)+((this.cloudStorage == null)? 0 :this.cloudStorage.hashCode()));
        result = ((result* 31)+((this.dateRestriction == null)? 0 :this.dateRestriction.hashCode()));
        result = ((result* 31)+((this.geographicalRestrictions == null)? 0 :this.geographicalRestrictions.hashCode()));
        result = ((result* 31)+((this.recontactMay == null)? 0 :this.recontactMay.hashCode()));
        result = ((result* 31)+((this.genomicResults == null)? 0 :this.genomicResults.hashCode()));
        result = ((result* 31)+((this.nonBiomedical == null)? 0 :this.nonBiomedical.hashCode()));
        result = ((result* 31)+((this.manualReview == null)? 0 :this.manualReview.hashCode()));
        result = ((result* 31)+((this.stigmatizeDiseases == null)? 0 :this.stigmatizeDiseases.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DataUse) == false) {
            return false;
        }
        DataUse rhs = ((DataUse) other);
        return (((((((((((((((((((((((((((((((((((((((this.commercialUse == rhs.commercialUse)||((this.commercialUse!= null)&&this.commercialUse.equals(rhs.commercialUse)))&&((this.otherRestrictions == rhs.otherRestrictions)||((this.otherRestrictions!= null)&&this.otherRestrictions.equals(rhs.otherRestrictions))))&&((this.populationStructure == rhs.populationStructure)||((this.populationStructure!= null)&&this.populationStructure.equals(rhs.populationStructure))))&&((this.other == rhs.other)||((this.other!= null)&&this.other.equals(rhs.other))))&&((this.recontactingDataSubjects == rhs.recontactingDataSubjects)||((this.recontactingDataSubjects!= null)&&this.recontactingDataSubjects.equals(rhs.recontactingDataSubjects))))&&((this.psychologicalTraits == rhs.psychologicalTraits)||((this.psychologicalTraits!= null)&&this.psychologicalTraits.equals(rhs.psychologicalTraits))))&&((this.gender == rhs.gender)||((this.gender!= null)&&this.gender.equals(rhs.gender))))&&((this.populationRestrictions == rhs.populationRestrictions)||((this.populationRestrictions!= null)&&this.populationRestrictions.equals(rhs.populationRestrictions))))&&((this.geneticStudiesOnly == rhs.geneticStudiesOnly)||((this.geneticStudiesOnly!= null)&&this.geneticStudiesOnly.equals(rhs.geneticStudiesOnly))))&&((this.generalUse == rhs.generalUse)||((this.generalUse!= null)&&this.generalUse.equals(rhs.generalUse))))&&((this.publicationResults == rhs.publicationResults)||((this.publicationResults!= null)&&this.publicationResults.equals(rhs.publicationResults))))&&((this.diseaseRestrictions == rhs.diseaseRestrictions)||((this.diseaseRestrictions!= null)&&this.diseaseRestrictions.equals(rhs.diseaseRestrictions))))&&((this.pediatric == rhs.pediatric)||((this.pediatric!= null)&&this.pediatric.equals(rhs.pediatric))))&&((this.vulnerablePopulations == rhs.vulnerablePopulations)||((this.vulnerablePopulations!= null)&&this.vulnerablePopulations.equals(rhs.vulnerablePopulations))))&&((this.collaborationInvestigators == rhs.collaborationInvestigators)||((this.collaborationInvestigators!= null)&&this.collaborationInvestigators.equals(rhs.collaborationInvestigators))))&&((this.genomicSummaryResults == rhs.genomicSummaryResults)||((this.genomicSummaryResults!= null)&&this.genomicSummaryResults.equals(rhs.genomicSummaryResults))))&&((this.methodsResearch == rhs.methodsResearch)||((this.methodsResearch!= null)&&this.methodsResearch.equals(rhs.methodsResearch))))&&((this.publicationMoratorium == rhs.publicationMoratorium)||((this.publicationMoratorium!= null)&&this.publicationMoratorium.equals(rhs.publicationMoratorium))))&&((this.aggregateResearch == rhs.aggregateResearch)||((this.aggregateResearch!= null)&&this.aggregateResearch.equals(rhs.aggregateResearch))))&&((this.sexualDiseases == rhs.sexualDiseases)||((this.sexualDiseases!= null)&&this.sexualDiseases.equals(rhs.sexualDiseases))))&&((this.controlSetOption == rhs.controlSetOption)||((this.controlSetOption!= null)&&this.controlSetOption.equals(rhs.controlSetOption))))&&((this.illegalBehavior == rhs.illegalBehavior)||((this.illegalBehavior!= null)&&this.illegalBehavior.equals(rhs.illegalBehavior))))&&((this.collaboratorRequired == rhs.collaboratorRequired)||((this.collaboratorRequired!= null)&&this.collaboratorRequired.equals(rhs.collaboratorRequired))))&&((this.populationOriginsAncestry == rhs.populationOriginsAncestry)||((this.populationOriginsAncestry!= null)&&this.populationOriginsAncestry.equals(rhs.populationOriginsAncestry))))&&((this.ethicsApprovalRequired == rhs.ethicsApprovalRequired)||((this.ethicsApprovalRequired!= null)&&this.ethicsApprovalRequired.equals(rhs.ethicsApprovalRequired))))&&((this.secondaryOther == rhs.secondaryOther)||((this.secondaryOther!= null)&&this.secondaryOther.equals(rhs.secondaryOther))))&&((this.addiction == rhs.addiction)||((this.addiction!= null)&&this.addiction.equals(rhs.addiction))))&&((this.hmbResearch == rhs.hmbResearch)||((this.hmbResearch!= null)&&this.hmbResearch.equals(rhs.hmbResearch))))&&((this.recontactMust == rhs.recontactMust)||((this.recontactMust!= null)&&this.recontactMust.equals(rhs.recontactMust))))&&((this.genomicPhenotypicData == rhs.genomicPhenotypicData)||((this.genomicPhenotypicData!= null)&&this.genomicPhenotypicData.equals(rhs.genomicPhenotypicData))))&&((this.cloudStorage == rhs.cloudStorage)||((this.cloudStorage!= null)&&this.cloudStorage.equals(rhs.cloudStorage))))&&((this.dateRestriction == rhs.dateRestriction)||((this.dateRestriction!= null)&&this.dateRestriction.equals(rhs.dateRestriction))))&&((this.geographicalRestrictions == rhs.geographicalRestrictions)||((this.geographicalRestrictions!= null)&&this.geographicalRestrictions.equals(rhs.geographicalRestrictions))))&&((this.recontactMay == rhs.recontactMay)||((this.recontactMay!= null)&&this.recontactMay.equals(rhs.recontactMay))))&&((this.genomicResults == rhs.genomicResults)||((this.genomicResults!= null)&&this.genomicResults.equals(rhs.genomicResults))))&&((this.nonBiomedical == rhs.nonBiomedical)||((this.nonBiomedical!= null)&&this.nonBiomedical.equals(rhs.nonBiomedical))))&&((this.manualReview == rhs.manualReview)||((this.manualReview!= null)&&this.manualReview.equals(rhs.manualReview))))&&((this.stigmatizeDiseases == rhs.stigmatizeDiseases)||((this.stigmatizeDiseases!= null)&&this.stigmatizeDiseases.equals(rhs.stigmatizeDiseases))));
    }

    public enum AggregateResearch {

        YES("Yes"),
        NO("No"),
        UNSPECIFIED("Unspecified");
        private final String value;
        private final static Map<String, DataUse.AggregateResearch> CONSTANTS = new HashMap<String, DataUse.AggregateResearch>();

        static {
            for (DataUse.AggregateResearch c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AggregateResearch(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static DataUse.AggregateResearch fromValue(String value) {
            DataUse.AggregateResearch constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum CloudStorage {

        YES("Yes"),
        NO("No"),
        UNSPECIFIED("Unspecified");
        private final String value;
        private final static Map<String, DataUse.CloudStorage> CONSTANTS = new HashMap<String, DataUse.CloudStorage>();

        static {
            for (DataUse.CloudStorage c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CloudStorage(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static DataUse.CloudStorage fromValue(String value) {
            DataUse.CloudStorage constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ControlSetOption {

        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");
        private final String value;
        private final static Map<String, DataUse.ControlSetOption> CONSTANTS = new HashMap<String, DataUse.ControlSetOption>();

        static {
            for (DataUse.ControlSetOption c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ControlSetOption(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static DataUse.ControlSetOption fromValue(String value) {
            DataUse.ControlSetOption constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Gender {

        FEMALE("Female"),
        MALE("Male");
        private final String value;
        private final static Map<String, DataUse.Gender> CONSTANTS = new HashMap<String, DataUse.Gender>();

        static {
            for (DataUse.Gender c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Gender(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static DataUse.Gender fromValue(String value) {
            DataUse.Gender constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum GenomicPhenotypicData {

        YES("Yes"),
        NO("No"),
        UNSPECIFIED("Unspecified");
        private final String value;
        private final static Map<String, DataUse.GenomicPhenotypicData> CONSTANTS = new HashMap<String, DataUse.GenomicPhenotypicData>();

        static {
            for (DataUse.GenomicPhenotypicData c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        GenomicPhenotypicData(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static DataUse.GenomicPhenotypicData fromValue(String value) {
            DataUse.GenomicPhenotypicData constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
