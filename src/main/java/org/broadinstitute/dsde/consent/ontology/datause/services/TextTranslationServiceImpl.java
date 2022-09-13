package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.shaded.com.google.common.reflect.TypeToken;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.google.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseElement;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;
import org.broadinstitute.dsde.consent.ontology.model.TermItem;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.slf4j.Logger;

public class TextTranslationServiceImpl implements TextTranslationService {

    private final Logger log = Utils.getLogger(this.getClass());

    private static final String YES = "Yes";
    private static final String FEMALE = "Female";
    private static final String MALE = "Male";

    /**
     * Consent Codes come in three flavors, *_POS, *_NEG, and *_NA for when a DUR question is
     * positively answered, negatively answered, or not answered at all.
     * See https://broadinstitute.atlassian.net/browse/GAWB-1634 for more details
     */
    private static final String GRU_POS = "Data is available for general research use. [GRU]";
    private static final String HMB_POS = "Data is limited for health/medical/biomedical research. [HMB]";
    private static final String DS_POS = "Data use is limited for studying: %s [DS]";
    private static final String POA_POS = "Future use for population origins or ancestry research is prohibited. [POA]";
    private static final String NCU_POS = "Commercial use prohibited. [NCU]";
    private static final String NCU_NEG = "Commercial use is not prohibited.";
    private static final String NCU_NA = NCU_NEG;
    private static final String NMDS_POS = "Data use for methods development research ONLY within the bounds of other data use limitations. [NMDS]";
    private static final String NMDS_NEG = "Data use for methods development research irrespective of the specified data use limitations is not prohibited.";
    private static final String NMDS_NA = NMDS_NEG;
    private static final String NCTRL_POS = "Future use as a control set for diseases other than those specified is prohibited. [NCTRL]";
    private static final String NCTRL_NEG = "Future use as a control set for any type of health/medical/biomedical study is not prohibited.";
    private static final String NCTRL_NA = "Restrictions for use as a control set for diseases other than those defined were not specified.";
    private static final String RS_M_POS = "Data use is limited to research on males. [RS-M]";
    private static final String RS_FM_POS = "Data use is limited to research on females. [RS-FM]";
    private static final String RS_POS = "Data use is limited to research on population ontology ID(s): %s [RS]";
    private static final String RS_PD_POS = "Data use is limited to pediatric research. [RS-PD]";
    private static final String DATE_POS = "Data distributor must verify that data from samples collected before %s will not be shared.";
    private static final String AGGREGATE_POS = "Aggregate level data for general research use is prohibited.";
    private static final String MANUAL_REVIEW = "Data access requests will require manual review.";
    private static final String GENETIC_STUDIES_ONLY = "Future use is limited to genetic studies only [GSO]";
    private static final String PUBLICATION_REQUIRED = "Publishing results of studies using the data available to the larger scientific community is required";
    private static final String GENOMIC_RESULTS = "Genomic summary results from this study are available only through controlled-access";
    private static final String COLLABORATION_REQUIRED = "Collaboration with the primary study investigators required.";

    // Terms of use/notes
    private static final String RECONTACT_MAY = "Subject re-contact may occur in certain circumstances, as specified: %s";
    private static final String RECONTACT_MUST = "Subject re-contact must occur in certain circumstances, as specified: %s";
    private static final String CLOUD_PROHIBITED = "Data storage on the cloud is prohibited.";
    private static final String ETHICS_APPROVAL = "Local ethics committee approval is required.";
    private static final String GEO_RESTRICTION = "Geographical restrictions: %s.";
    private static final String OTHER_POS = "Other restrictions: %s.";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private final AutocompleteService autocompleteService;

    private final GCSStore gcsStore;

    @Inject
    public TextTranslationServiceImpl(AutocompleteService autocompleteService, GCSStore gcsStore) {
        this.autocompleteService = autocompleteService;
        this.gcsStore = gcsStore;
    }

    @Override
    public DataUseSummary translateDataUseSummary(String dataUseString) {
        DataUse dataUse = new Gson().fromJson(dataUseString, DataUse.class);
        return translateSummary(dataUse);
    }

    @Override
    public String translateDataset(String dataUseString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DataUse dataUse = mapper.readValue(dataUseString, DataUse.class);
        return translate(dataUse, TranslateFor.DATASET);
    }

    @Override
    public Map<String, Recommendation> translateParagraph(String paragraph) throws IOException {
      Map<String, Recommendation> recommendations = new HashMap<>();

      List<TermItem> terms = loadTermsFromGoogleStorage();

      if (terms.isEmpty()) {
        throw new IOException("Unable to process search terms");
      }

      for (TermItem term : terms) {
        final String title = term.getTitle();
        final String category = term.getCategory();
        final String url = term.getUrl();
        final String[] keywords = term.getKeywords();

        for (String keyword : keywords) {
          final boolean foundMatch = searchForKeyword(keyword, paragraph);
          if (foundMatch) {
            recommendations.computeIfAbsent(url, key -> new Recommendation(
                title,
                category
            ));
            break;
          }
        }
      }

      return recommendations;
    }

    @Override
    public String translatePurpose(String dataUseString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DataUse dataUse = mapper.readValue(dataUseString, DataUse.class);
        return translate(dataUse, TranslateFor.PURPOSE);
    }

    private static boolean searchForKeyword(final String keyword, final String targetText) {
      return StringUtils.containsIgnoreCase(targetText, keyword);
    }

    private List<TermItem> loadTermsFromGoogleStorage() {
      try {
        HttpResponse response = gcsStore.getStorageDocument("ontology/search-terms.json");
        String terms = response.parseAsString();
        return new Gson().fromJson(
            terms,
            new TypeToken<List<TermItem>>() {
            }.getType()
        );
      } catch (Exception e) {
        log.error("Error processing search terms from GCS: " + e);
      }
      return List.of();
    }

    /**
     * Generate a structured summary of the data use.
     *
     * @param dataUse The DataUse
     * @return DataUseSummary The structured summary
     */
    private DataUseSummary translateSummary(DataUse dataUse) {
        DataUseSummary summary = new DataUseSummary();
        List<DataUseElement> primary = new ArrayList<>();
        List<DataUseElement> secondary = new ArrayList<>();
        if (Objects.isNull(dataUse)) {
            return summary;
        }
        if (BooleanUtils.isTrue(dataUse.getGeneralUse())) {
            primary.add(new DataUseElement("GRU", GRU_POS));
        }
        if (BooleanUtils.isTrue(dataUse.getHmbResearch())) {
            primary.add(new DataUseElement("HMB", HMB_POS));
        }

        if (!dataUse.getDiseaseRestrictions().isEmpty()) {
            List<String> labels = new ArrayList<>();
            dataUse.getDiseaseRestrictions().forEach(r -> {
                try {
                    List<TermResource> terms = autocompleteService.lookupById(r);
                    if (!terms.isEmpty()) {
                        labels.add(terms.get(0).label);
                    } else {
                        log.warn("No terms returned for: " + r);
                    }
                } catch (IOException e) {
                    log.warn("Unable to retrieve term id: " + r);
                }
            });
            if (!labels.isEmpty()) {
                String dsRestrictions = labels
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .collect(Collectors.joining(", "));
                primary.add(new DataUseElement("DS", String.format(DS_POS, dsRestrictions)));
            }
        }
        if (BooleanUtils.isTrue(dataUse.getPopulationOriginsAncestry())) {
            primary.add(new DataUseElement("NPOA", POA_POS));
        }

        if (BooleanUtils.isTrue(dataUse.getCommercialUse())) {
            secondary.add(new DataUseElement("NCU", NCU_NA));
        } else {
            if (BooleanUtils.isTrue(dataUse.getCommercialUse())) {
                secondary.add(new DataUseElement("NCU", NCU_POS));
            } else {
                secondary.add(new DataUseElement("NCU", NCU_NEG));
            }
        }
        if (BooleanUtils.isTrue(dataUse.getMethodsResearch())) {
            secondary.add(new DataUseElement("NMDS", NMDS_NA));
        } else {
            if (dataUse.getMethodsResearch() != null) {
                secondary.add(new DataUseElement("NMDS", NMDS_POS));
            } else {
                secondary.add(new DataUseElement("NMDS", NMDS_NEG));
            }
        }

        if (dataUse.getControlSetOption() == null || dataUse.getControlSetOption().equalsIgnoreCase("Unspecified")) {
            secondary.add(new DataUseElement("NCTRL", NCTRL_NA));
        } else {
            if (dataUse.getControlSetOption().equalsIgnoreCase("Yes")) {
                secondary.add(new DataUseElement("NCTRL", NCTRL_POS));
            } else {
                secondary.add(new DataUseElement("NCTRL", NCTRL_NEG));
            }
        }

        if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(MALE)) {
            secondary.add(new DataUseElement("POP-M", RS_M_POS));
        }
        if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(FEMALE)) {
            secondary.add(new DataUseElement("POP-F", RS_FM_POS));
        }

        // TODO: In ORSP, we query DatabioOntology services, not consent.
        if (!dataUse.getPopulationRestrictions().isEmpty()) {
            String popRestrictions = dataUse.getPopulationRestrictions()
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(", "));
            secondary.add(new DataUseElement("POP", String.format(RS_POS, popRestrictions)));
        }
        if (BooleanUtils.isTrue(dataUse.getPediatric())) {
            secondary.add(new DataUseElement("POP-PD", RS_PD_POS));
        }
        if (StringUtils.isNotBlank(dataUse.getDateRestriction())) {
            try {
                String date = DATE_FORMAT.format(dataUse.getDateRestriction());
                secondary.add(new DataUseElement("TS", String.format(DATE_POS, date)));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid date format for: " + dataUse.getDateRestriction());
            }
        }
        if (Optional.ofNullable(dataUse.getAggregateResearch()).orElse("na").equalsIgnoreCase(YES)) {
            secondary.add(new DataUseElement("NAGG", AGGREGATE_POS));
        }
        if (dataUse.getRecontactMay() != null) {
            secondary.add(new DataUseElement("OTHER", String.format(RECONTACT_MAY, dataUse.getRecontactMay())));
        }
        if (StringUtils.isNotBlank(dataUse.getRecontactMust())) {
            secondary.add(new DataUseElement("OTHER", String.format(RECONTACT_MUST, dataUse.getRecontactMust())));
        }
        if (Optional.ofNullable(dataUse.getCloudStorage()).orElse("na").equalsIgnoreCase(YES)) {
            secondary.add(new DataUseElement("OTHER", CLOUD_PROHIBITED));
        }
        if (StringUtils.isNotBlank(dataUse.getGeographicalRestrictions())) {
            secondary.add(new DataUseElement("GS", String.format(GEO_RESTRICTION, dataUse.getGeographicalRestrictions())));
        }
        if (StringUtils.isNotBlank(dataUse.getOther())) {
            secondary.add(new DataUseElement("OTHER", String.format(OTHER_POS, dataUse.getOther())));
        }
        if (BooleanUtils.isTrue(dataUse.getEthicsApprovalRequired())) {
            secondary.add(new DataUseElement("IRB", ETHICS_APPROVAL));
        }
        if (BooleanUtils.isTrue(dataUse.getCollaboratorRequired())) {
            secondary.add(new DataUseElement("COL", COLLABORATION_REQUIRED));
        }
        if (BooleanUtils.isTrue(dataUse.getManualReview())) {
            secondary.add(new DataUseElement("OTHER", MANUAL_REVIEW));
        }
        if (BooleanUtils.isTrue(dataUse.getGeneticStudiesOnly())) {
            secondary.add(new DataUseElement("GSO", GENETIC_STUDIES_ONLY));
        }
        if (BooleanUtils.isTrue(dataUse.getPublicationResults())) {
            secondary.add(new DataUseElement("PUB", PUBLICATION_REQUIRED));
        }
        if (BooleanUtils.isTrue(dataUse.getGenomicResults())) {
            secondary.add(new DataUseElement("OTHER", GENOMIC_RESULTS));
        }
        if (StringUtils.isNotBlank(dataUse.getGenomicSummaryResults())) {
            secondary.add(new DataUseElement("OTHER", dataUse.getGenomicSummaryResults()));
        }
        if (BooleanUtils.isTrue(dataUse.getCollaborationInvestigators())) {
            secondary.add(new DataUseElement("COL", COLLABORATION_REQUIRED));
        }

        summary.setPrimary(primary);
        summary.setSecondary(secondary);
        return summary;
    }

    /**
     * Generate a textual summary that consists of an ordered list of restrictions.
     *
     * @param dataUse The DataUse object
     * @return Summary list of restrictions in string format
     */
    public String translate(DataUse dataUse, TranslateFor translateFor) {
        List<String> summary = new ArrayList<>();
        if (translateFor.equals(TranslateFor.DATASET)) {
            summary.add("Samples are restricted for use under the following conditions:");
        } else if (translateFor.equals(TranslateFor.PURPOSE)) {
            summary.add("Research is limited to samples restricted for use under the following conditions:");
        }

        if (dataUse == null) {
            return String.join("\n", summary);
        }
        if (BooleanUtils.isTrue(dataUse.getGeneralUse())) summary.add(GRU_POS);
        if (BooleanUtils.isTrue(dataUse.getHmbResearch())) summary.add(HMB_POS);

        if (!dataUse.getDiseaseRestrictions().isEmpty()) {
            List<String> labels = new ArrayList<>();
            dataUse.getDiseaseRestrictions().forEach(r -> {
                try {
                    List<TermResource> terms = autocompleteService.lookupById(r);
                    if (!terms.isEmpty()) {
                        labels.add(terms.get(0).label);
                    } else {
                        log.warn("No terms returned for: " + r);
                    }
                } catch (IOException e) {
                    log.warn("Unable to retrieve term id: " + r);
                }
            });
            if (!labels.isEmpty()) {
                String dsRestrictions = labels
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .collect(Collectors.joining(", "));
                summary.add(String.format(DS_POS, dsRestrictions));
            }
        }
        if (BooleanUtils.isTrue(dataUse.getPopulationOriginsAncestry())) {
            summary.add(POA_POS);
        }

        if (BooleanUtils.isTrue(dataUse.getCommercialUse())) {
            summary.add(NCU_NA);
        } else {
            if (BooleanUtils.isTrue(dataUse.getCommercialUse())) {
                summary.add(NCU_POS);
            } else {
                summary.add(NCU_NEG);
            }
        }
        if (BooleanUtils.isTrue(dataUse.getMethodsResearch())) {
            summary.add(NMDS_NA);
        } else {
            if (dataUse.getMethodsResearch() != null) {
                summary.add(NMDS_POS);
            } else {
                summary.add(NMDS_NEG);
            }
        }

        if (dataUse.getControlSetOption() == null || dataUse.getControlSetOption().equalsIgnoreCase("Unspecified")) {
            summary.add(NCTRL_NA);
        } else {
            if (dataUse.getControlSetOption().equalsIgnoreCase("Yes")) {
                summary.add(NCTRL_POS);
            } else {
                summary.add(NCTRL_NEG);
            }
        }

        if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(MALE)) summary.add(RS_M_POS);
        if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(FEMALE)) summary.add(RS_FM_POS);

        // TODO: In ORSP, we query DatabioOntology services, not consent.
        if (!dataUse.getPopulationRestrictions().isEmpty()) {
            String popRestrictions = dataUse.getPopulationRestrictions()
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(", "));
            summary.add(String.format(RS_POS, popRestrictions));
        }
        if (BooleanUtils.isTrue(dataUse.getPediatric())) {
            summary.add(RS_PD_POS);
        }
        if (StringUtils.isNotBlank(dataUse.getDateRestriction())) {
            try {
                String date = DATE_FORMAT.format(dataUse.getDateRestriction());
                summary.add(String.format(DATE_POS, date));
            } catch (IllegalArgumentException e) {
                log.warn("Invalid date format for: " + dataUse.getDateRestriction());
            }
        }
        if (Optional.ofNullable(dataUse.getAggregateResearch()).orElse("na").equalsIgnoreCase(YES)) {
            summary.add(AGGREGATE_POS);
        }
        if (dataUse.getRecontactMay() != null) {
            summary.add(String.format(RECONTACT_MAY, dataUse.getRecontactMay()));
        }
        if (StringUtils.isNotBlank(dataUse.getRecontactMust())) {
            summary.add(String.format(RECONTACT_MUST, dataUse.getRecontactMust()));
        }
        if (Optional.ofNullable(dataUse.getCloudStorage()).orElse("na").equalsIgnoreCase(YES)) {
            summary.add(CLOUD_PROHIBITED);
        }
        if (StringUtils.isNotBlank(dataUse.getGeographicalRestrictions())) {
            summary.add(String.format(GEO_RESTRICTION, dataUse.getGeographicalRestrictions()));
        }
        if (StringUtils.isNotBlank(dataUse.getOther())) {
            summary.add(String.format(OTHER_POS, dataUse.getOther()));
        }
        if (BooleanUtils.isTrue(dataUse.getEthicsApprovalRequired())) {
            summary.add(ETHICS_APPROVAL);
        }
        if (BooleanUtils.isTrue(dataUse.getCollaboratorRequired())) {
            summary.add(COLLABORATION_REQUIRED);
        }
        if (BooleanUtils.isTrue(dataUse.getManualReview())) {
            summary.add(MANUAL_REVIEW);
        }
        if (BooleanUtils.isTrue(dataUse.getGeneticStudiesOnly())) {
            summary.add(GENETIC_STUDIES_ONLY);
        }
        if (BooleanUtils.isTrue(dataUse.getPublicationResults())) {
            summary.add(PUBLICATION_REQUIRED);
        }
        if (BooleanUtils.isTrue(dataUse.getGenomicResults())) {
            summary.add(GENOMIC_RESULTS);
        }
        if (StringUtils.isNotBlank(dataUse.getGenomicSummaryResults())) {
            summary.add(dataUse.getGenomicSummaryResults());
        }
        if (BooleanUtils.isTrue(dataUse.getCollaborationInvestigators())) {
            summary.add(COLLABORATION_REQUIRED);
        }

        return String.join("\n", summary);
    }

}
