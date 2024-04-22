package org.broadinstitute.dsde.consent.ontology.datause.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseElement;
import org.broadinstitute.dsde.consent.ontology.model.DataUseSummary;
import org.broadinstitute.dsde.consent.ontology.model.Recommendation;
import org.broadinstitute.dsde.consent.ontology.model.TermItem;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;

public class TextTranslationServiceImpl implements TextTranslationService, OntologyLogger {

  private static final String FEMALE = "Female";
  private static final String MALE = "Male";
  private static final String GRU = "Data is available for general research use. [GRU]";
  private static final String DS = "Data use is limited for studying: %s [DS]";
  private static final String HMB = "Data is limited for health/medical/biomedical research. [HMB]";
  private static final String POA = "Future use for population origins or ancestry research is prohibited. [POA]";
  private static final String NMDS = "Data use for methods development research ONLY within the bounds of other data use limitations. [NMDS]";
  private static final String NCU = "Commercial use prohibited. [NCU]";
  private static final String OTHER = "Other restrictions: %s.";
  private static final String SECONDARY_OTHER = "Secondary other restrictions: %s.";
  private static final String ETHICS_APPROVAL = "Local ethics committee approval is required.";
  private static final String COLLABORATION_REQUIRED = "Collaboration with the primary study investigators required. [COL]";
  private static final String GEO_RESTRICTION = "Geographical restrictions: %s.";
  private static final String GSO = "Future use is limited to genetic studies only [GSO]";
  private static final String PUB_REQUIRED = "Publishing results of studies using the data available to the larger scientific community is required";
  private static final String PUB_MORATORIUM = "Publishing moratorium until '%s' is in effect. [MOR]";
  private static final String NCTRL = "Future use as a control set for diseases other than those specified is prohibited. [NCTRL]";
  private static final String RS_M = "Data use is limited to research on males. [RS-M]";
  private static final String RS_FM = "Data use is limited to research on females. [RS-FM]";
  private static final String RS_PD = "Data use is limited to pediatric research. [RS-PD]";
  private static final String POP = "Future use for study variation in the general population (e.g. calling variants and/or studying their distribution). [POP]";

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
      logException("Error processing search terms from GCS", e);
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
      primary.add(new DataUseElement("GRU", GRU));
    }

    if (dataUse.getDiseaseRestrictions() != null && !dataUse.getDiseaseRestrictions().isEmpty()) {
      List<String> labels = new ArrayList<>();
      dataUse.getDiseaseRestrictions().forEach(r -> {
        try {
          List<TermResource> terms = autocompleteService.lookupById(r);
          if (!terms.isEmpty()) {
            labels.add(terms.get(0).label);
          } else {
            logWarn("No terms returned for: " + r);
          }
        } catch (IOException e) {
          logWarn("Unable to retrieve term id: " + r);
        }
      });
      if (!labels.isEmpty()) {
        String dsRestrictions = labels
            .stream()
            .filter(Objects::nonNull)
            .filter(r -> !r.isEmpty())
            .collect(Collectors.joining(", "));
        primary.add(new DataUseElement("DS", String.format(DS, dsRestrictions)));
      }
    }

    if (BooleanUtils.isTrue(dataUse.getHmbResearch())) {
      primary.add(new DataUseElement("HMB", HMB));
    }

    if (BooleanUtils.isTrue(dataUse.getPopulationOriginsAncestry())) {
      primary.add(new DataUseElement("NPOA", POA));
    }

    if (StringUtils.isNotBlank(dataUse.getOther())) {
      primary.add(new DataUseElement("OTHER", String.format(OTHER, dataUse.getOther())));
    }

    if (BooleanUtils.isTrue(dataUse.getMethodsResearch())) {
      secondary.add(new DataUseElement("NMDS", NMDS));
    }

    if (BooleanUtils.isTrue(dataUse.getNonProfitUse())) {
      secondary.add(new DataUseElement("NCU", NCU));
    }

    if (StringUtils.isNotBlank(dataUse.getSecondaryOther())) {
      secondary.add(new DataUseElement("OTHER", String.format(SECONDARY_OTHER, dataUse.getSecondaryOther())));
    }

    if (BooleanUtils.isTrue(dataUse.getEthicsApprovalRequired())) {
      secondary.add(new DataUseElement("IRB", ETHICS_APPROVAL));
    }

    if (BooleanUtils.isTrue(dataUse.getCollaboratorRequired())) {
      secondary.add(new DataUseElement("COL", COLLABORATION_REQUIRED));
    }

    if (StringUtils.isNotBlank(dataUse.getGeographicalRestrictions())) {
      secondary.add(new DataUseElement("GS",
          String.format(GEO_RESTRICTION, dataUse.getGeographicalRestrictions())));
    }

    if (BooleanUtils.isTrue(dataUse.getGeneticStudiesOnly())) {
      secondary.add(new DataUseElement("GSO", GSO));
    }

    if (BooleanUtils.isTrue(dataUse.getPublicationResults())) {
      secondary.add(new DataUseElement("PUB", PUB_REQUIRED));
    }

    if (StringUtils.isNotBlank(dataUse.getPublicationMoratorium())) {
      secondary.add(new DataUseElement("MOR", String.format(PUB_MORATORIUM, dataUse.getPublicationMoratorium())));
    }

    if (BooleanUtils.isTrue(dataUse.getControls())) {
      secondary.add(new DataUseElement("NCTRL", NCTRL));
    }

    if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(MALE)) {
      secondary.add(new DataUseElement("POP-M", RS_M));
    }

    if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(FEMALE)) {
      secondary.add(new DataUseElement("POP-F", RS_FM));
    }

    if (BooleanUtils.isTrue(dataUse.getPediatric())) {
      secondary.add(new DataUseElement("POP-PD", RS_PD));
    }

    if (BooleanUtils.isTrue(dataUse.getPopulation())) {
      secondary.add(new DataUseElement("POP", POP));
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
      summary.add(
          "Research is limited to samples restricted for use under the following conditions:");
    }

    if (dataUse == null) {
      return String.join("\n", summary);
    }
    if (BooleanUtils.isTrue(dataUse.getGeneralUse())) {
      summary.add(GRU);
    }

    if (dataUse.getDiseaseRestrictions() != null && !dataUse.getDiseaseRestrictions().isEmpty()) {
      List<String> labels = new ArrayList<>();
      dataUse.getDiseaseRestrictions().forEach(r -> {
        try {
          List<TermResource> terms = autocompleteService.lookupById(r);
          if (!terms.isEmpty()) {
            labels.add(terms.get(0).label);
          } else {
            logWarn("No terms returned for: " + r);
          }
        } catch (IOException e) {
          logWarn("Unable to retrieve term id: " + r);
        }
      });
      if (!labels.isEmpty()) {
        String dsRestrictions = labels
            .stream()
            .filter(Objects::nonNull)
            .filter(r -> !r.isEmpty())
            .collect(Collectors.joining(", "));
        summary.add(String.format(DS, dsRestrictions));
      }
    }

    if (BooleanUtils.isTrue(dataUse.getHmbResearch())) {
      summary.add(HMB);
    }

    if (BooleanUtils.isTrue(dataUse.getPopulationOriginsAncestry())) {
      summary.add(POA);
    }

    if (BooleanUtils.isTrue(dataUse.getMethodsResearch())) {
      summary.add(NMDS);
    }

    if (BooleanUtils.isTrue(dataUse.getNonProfitUse())) {
      summary.add(NCU);
    }

    if (StringUtils.isNotBlank(dataUse.getOther())) {
      summary.add(String.format(OTHER, dataUse.getOther()));
    }

    if (StringUtils.isNotBlank(dataUse.getSecondaryOther())) {
      summary.add(String.format(SECONDARY_OTHER, dataUse.getSecondaryOther()));
    }

    if (BooleanUtils.isTrue(dataUse.getEthicsApprovalRequired())) {
      summary.add(ETHICS_APPROVAL);
    }

    if (BooleanUtils.isTrue(dataUse.getCollaboratorRequired())) {
      summary.add(COLLABORATION_REQUIRED);
    }

    if (StringUtils.isNotBlank(dataUse.getGeographicalRestrictions())) {
      summary.add(String.format(GEO_RESTRICTION, dataUse.getGeographicalRestrictions()));
    }

    if (BooleanUtils.isTrue(dataUse.getGeneticStudiesOnly())) {
      summary.add(GSO);
    }

    if (BooleanUtils.isTrue(dataUse.getPublicationResults())) {
      summary.add(PUB_REQUIRED);
    }

    if (StringUtils.isNotBlank(dataUse.getPublicationMoratorium())) {
      summary.add(String.format(PUB_MORATORIUM, dataUse.getPublicationMoratorium()));
    }

    if (BooleanUtils.isTrue(dataUse.getControls())) {
      summary.add(NCTRL);
    }

    if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(MALE)) {
      summary.add(RS_M);
    }

    if (Optional.ofNullable(dataUse.getGender()).orElse("na").equalsIgnoreCase(FEMALE)) {
      summary.add(RS_FM);
    }

    if (BooleanUtils.isTrue(dataUse.getPediatric())) {
      summary.add(RS_PD);
    }

    if (BooleanUtils.isTrue(dataUse.getPopulation())) {
      summary.add(POP);
    }

    return String.join("\n", summary);
  }

}
