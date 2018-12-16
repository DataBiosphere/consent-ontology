package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextTranslationServiceImpl implements TextTranslationService {

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

    // Terms of use/notes
    private static final String RECONTACT_MAY = "Subject re-contact may occur in certain circumstances, as specified: %s";
    private static final String RECONTACT_MUST = "Subject re-contact must occur in certain circumstances, as specified: %s";
    private static final String CLOUD_PROHIBITED = "Data storage on the cloud is prohibited.";
    private static final String ETHICS_APPROVAL = "Local ethics committee approval is required.";
    private static final String GEO_RESTRICTION = "Geographical restrictions: %s.";
    private static final String OTHER_POS = "Other restrictions: %s.";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public TextTranslationServiceImpl() { }

    @Override
    public String translateDataset(String dataUseString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DataUse dataUse = mapper.readValue(dataUseString, DataUse.class);
        return translate(dataUse, TranslateFor.DATASET);
    }

    @Override
    public String translatePurpose(String dataUseString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DataUse dataUse = mapper.readValue(dataUseString, DataUse.class);
        return translate(dataUse, TranslateFor.PURPOSE);
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
        if (Optional.ofNullable(dataUse.getGeneralUse()).orElse(false)) summary.add(GRU_POS);
        if (Optional.ofNullable(dataUse.getHmbResearch()).orElse(false)) summary.add(HMB_POS);

        // TODO: Need to query ES for the term label ... can't just use the ID here.
        if (!dataUse.getDiseaseRestrictions().isEmpty()) {
            String dsRestrictions = dataUse.getDiseaseRestrictions()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(r -> !r.isEmpty())
                    .collect(Collectors.joining(", "));
            summary.add(String.format(DS_POS, dsRestrictions));
        }
        if (Optional.ofNullable(dataUse.getPopulationOriginsAncestry()).orElse(false)) {
            summary.add(POA_POS);
        }

        if (dataUse.getCommercialUse() == null) {
            summary.add(NCU_NA);
        } else {
            if (dataUse.getCommercialUse() != null) {
                summary.add(NCU_POS);
            } else {
                summary.add(NCU_NEG);
            }
        }
        if (dataUse.getMethodsResearch() == null) {
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
                    .filter(Objects::nonNull)
                    .filter(r -> !r.isEmpty())
                    .collect(Collectors.joining(", "));
            summary.add(String.format(RS_POS, popRestrictions ));
        }
        if (Optional.ofNullable(dataUse.getPediatric()).orElse(false)) {
            summary.add(RS_PD_POS);
        }
        if (dataUse.getDateRestriction() != null) {
            summary.add(String.format(DATE_POS, DATE_FORMAT.format(dataUse.getDateRestriction())));
        }
        if (Optional.ofNullable(dataUse.getAggregateResearch()).orElse("na").equalsIgnoreCase(YES)) {
            summary.add(AGGREGATE_POS);
        }
        if (dataUse.getRecontactMay() != null) {
            summary.add(String.format(RECONTACT_MAY, dataUse.getRecontactMay()));
        }
        if (dataUse.getRecontactMust() != null) {
            summary.add(String.format(RECONTACT_MUST, dataUse.getRecontactMust()));
        }
        if (Optional.ofNullable(dataUse.getCloudStorage()).orElse("na").equalsIgnoreCase(YES)) {
            summary.add(CLOUD_PROHIBITED);
        }
        if (dataUse.getGeographicalRestrictions() != null && !dataUse.getGeographicalRestrictions().isEmpty()) {
            summary.add(String.format(GEO_RESTRICTION, dataUse.getGeographicalRestrictions()));
        }
        if (dataUse.getOther() != null && !dataUse.getOther().isEmpty()) {
            summary.add(String.format(OTHER_POS, dataUse.getOther()));
        }
        if (Optional.ofNullable(dataUse.getIrb()).orElse(false)) {
            summary.add(ETHICS_APPROVAL);
        }
        if (Optional.ofNullable(dataUse.getManualReview()).orElse(false)) {
            summary.add(MANUAL_REVIEW);
        }

        return String.join("\n", summary);
    }

}
