package org.broadinstitute.dsde.consent.ontology.datause;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DataUseMatchCasesV3 {
  private static final String HMB_F1 = "The GRU Research Purpose does not match the HMB data use limitations.";
  private static final String HMB_F2 = "The HMB Research Purpose does not match the Disease-Specific data use limitations.";
  private static final String DS_F1 = "The unspecified Disease Research Purpose does not match the Disease-Specific data use limitations.";
  private static final String DS_F2 = "The Disease-Specific: %s Research Purpose is not a valid subclass of the Disease-Specific data use limitations.";
  private static final String MDS_F1 = "The Methods development Research Purpose does not match the Disease-Specific data use limitations.";
  private static final String POA_F1 = "The Populations, Origins, Ancestry Research Purpose does not match the HMB or Disease-Specific data use limitation.";
  private static final String NCU_F1 = "The Commercial Use Research Purpose does not match the No Commercial Use data use limitation.";

  /**
   * RP: Disease Focused Research
   * Future use is limited to research involving the following disease area(s) [DS]
   * Datasets:
   *      Any dataset with GRU=true
   *      Any dataset with HMB=true
   *      Any dataset tagged to this disease exactly
   *      Any dataset tagged to a DOID ontology Parent of disease X
   *
   * @param purpose The data use object representing the Research Purpose
   * @param dataset The data use object representing the Dataset
   * @param purposeDiseaseIdMap is a map of each purpose term id to a list of that term's parent term ids
   */
  static ImmutablePair<Boolean, List<String>> matchDiseases(
      DataUseV3 purpose, DataUseV3 dataset, Map<String, List<String>> purposeDiseaseIdMap) {
    // short-circuit if no disease focused research
    if (purpose.getDiseaseRestrictions().isEmpty() && dataset.getDiseaseRestrictions().isEmpty()) {
      return ImmutablePair.of(true, Collections.emptyList());
    }
    // short-circuit if dataset is GRU
    if (getNullableOrFalse(dataset.getGeneralUse())) {
      return ImmutablePair.of(true, Collections.emptyList());
    }
    // short-circuit if dataset is HMB
    if (getNullableOrFalse(dataset.getHmbResearch())) {
      return ImmutablePair.of(true, Collections.emptyList());
    }
    // short-circuit if no diseases specified in the purpose, but the dataset specifies diseases
    if (!dataset.getDiseaseRestrictions().isEmpty() && purpose.getDiseaseRestrictions().isEmpty()) {
      return ImmutablePair.of(false, Collections.singletonList(DS_F1));
    }

    // We want all-purpose disease IDs to be a subclass of any dataset disease ID
    List<String> failures = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : purposeDiseaseIdMap.entrySet()) {
      boolean match = entry.getValue()
          .stream()
          .anyMatch(dataset.getDiseaseRestrictions()::contains);
      if (!match) {
        failures.add(String.format(DS_F2,  entry.getKey()));
      }
    }

    return ImmutablePair.of(failures.isEmpty(), failures);
  }

  /**
   * RP: Methods development/Validation study
   * Future use for methods research (analytic/software/technology development) outside the
   *      bounds of the other specified restrictions
   * Datasets:
   *      Any dataset where GRU = true
   *      Any dataset where HMB = true
   *      Any dataset where POA = true
   *      Any dataset where DS-X match
   */
  static ImmutablePair<Boolean, List<String>> matchMDS(DataUseV3 purpose, DataUseV3 dataset, boolean diseaseMatch) {
    // short-circuit if no disease focused research
    if (purpose.getDiseaseRestrictions().isEmpty() && dataset.getDiseaseRestrictions().isEmpty()) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    boolean purposeMDS = getNullableOrFalse(purpose.getMethodsResearch());
    boolean datasetGRU = getNullableOrFalse(dataset.getGeneralUse());
    boolean datasetHMB = getNullableOrFalse(dataset.getHmbResearch());
    boolean datasetPOA = getNullableOrFalse(dataset.getPopulationOriginsAncestry());

    // short-circuit if dataset is GRU
    if (datasetGRU) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    // short-circuit if dataset is HMB
    if (purposeMDS && datasetHMB) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    // short-circuit if dataset is POA
    if (purposeMDS && datasetPOA) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    // short-circuit if there is a disease match
    if (diseaseMatch) {
      return ImmutablePair.of(true, Collections.emptyList());
    } else {
      return ImmutablePair.of(false, Collections.singletonList(MDS_F1));
    }
  }

  /**
   * RP: Study population origins or ancestry
   * Future use is limited to research involving a specific population [POA]
   * Datasets:
   *      Any dataset tagged with GRU
   *      Any dataset tagged with POA
   */
  static ImmutablePair<Boolean, List<String>> matchPOA(DataUseV3 purpose, DataUseV3 dataset) {
    // short-circuit if no POA clause
    if (Objects.isNull(purpose.getPopulationOriginsAncestry())) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    boolean purposePOA = getNullableOrFalse(purpose.getPopulationOriginsAncestry());
    boolean datasetPOA = getNullableOrFalse(dataset.getPopulationOriginsAncestry());
    boolean datasetGRU = getNullableOrFalse(dataset.getGeneralUse());

    // short-circuit if dataset is GRU
    if (datasetGRU) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    // short-circuit if dataset is POA
    if (purposePOA && datasetPOA) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    List<String> failures = new ArrayList<>();
    if (!(purpose.getPopulationOriginsAncestry() &&
        getNullableOrFalse(dataset.getGeneralUse()))) {
      failures.add(POA_F1);
    }

    return ImmutablePair.of(failures.isEmpty(), failures);
  }

  /**
   * RP: Commercial purpose/by a commercial entity
   * Future commercial use is prohibited [NCU]
   * Future use by for-profit entities is prohibited [NPU]
   * Datasets:
   *      Any dataset where NPU and NCU are both false
   */
  static ImmutablePair<Boolean, List<String>> matchCommercial(DataUseV3 purpose, DataUseV3 dataset) {
    // short-circuit if no commercial/ non-profit clauses
    if (Objects.isNull(purpose.getCommercialUse())){
      return ImmutablePair.of(true, Collections.emptyList());
    }
    if ((Objects.isNull(dataset.getCommercialUse())) && (Objects.isNull(dataset.getNonProfitUse()))){
      return ImmutablePair.of(true, Collections.emptyList());
    }

    List<String> failures = new ArrayList<>();
    boolean purposeCommercial = getNullableOrFalse(purpose.getCommercialUse());
    boolean datasetCommercial = getNullableOrFalse(dataset.getCommercialUse());
    boolean datasetNPU = getNullableOrFalse(dataset.getNonProfitUse());

    if (purposeCommercial && !datasetCommercial) {
      failures.add(NCU_F1);
    }

    if ((purposeCommercial && datasetNPU)){
      failures.add(NCU_F1);
    }

    // short circuit if dataset is not NCU OR not NPU
    if ((purposeCommercial && datasetCommercial)) {
      return ImmutablePair.of(true, Collections.emptyList());
    } else {
      return ImmutablePair.of(failures.isEmpty(), failures);
    }
  }

  /**
   * RP: HMB
   * Datasets:
   *      Any dataset tagged with GRU
   *      Any dataset tagged with HMB
   *
   * @param purpose The data use object representing the Research Purpose
   * @param dataset The data use object representing the Dataset
   */
  static ImmutablePair<Boolean, List<String>> matchHMB(DataUseV3 purpose, DataUseV3 dataset) {
    // short-circuit hmb if not set
    if (Objects.isNull(purpose.getHmbResearch())&& (Objects.isNull(dataset.getHmbResearch()))) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    boolean purposeGRU = getNullableOrFalse(purpose.getGeneralUse());
    boolean purposeHMB = getNullableOrFalse(purpose.getHmbResearch());
    boolean datasetGRU = getNullableOrFalse(dataset.getGeneralUse());
    boolean datasetHMB = getNullableOrFalse(dataset.getHmbResearch());

    if (datasetHMB && purposeGRU) {
      return ImmutablePair.of(false, Collections.singletonList(HMB_F1));
    }

    // short-circuit if dataset is GRU
    if (datasetGRU) {
      return ImmutablePair.of(true, Collections.emptyList());
    }

    // short-circuit if dataset is HMB
    if (purposeHMB && datasetHMB) {
      return ImmutablePair.of(true, Collections.emptyList());
    } else {
      return ImmutablePair.of(false, Collections.singletonList(HMB_F2));
    }

  }

  // Helper Methods

  /**
   * @param bool nullable boolean value
   * @return boolean The value, or false otherwise
   */
  private static boolean getNullableOrFalse(Boolean bool) {
    return Optional.ofNullable(bool).orElse(false);
  }
}
